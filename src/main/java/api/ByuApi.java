package api;

import model.*;
import model.id.*;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.*;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ByuApi
{
	private static final String semesterApi = "http://saasta.byu.edu/noauth/classSchedule/ajax/getYeartermData.php";
	private static final String coursesApi = "http://saasta.byu.edu/noauth/classSchedule/ajax/getClasses.php";
	private static final String sectionsApi = "http://saasta.byu.edu/noauth/classSchedule/ajax/getSections.php";
	private static final char[] idChars = "ABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890".toCharArray();
	private static final int idLength = 20;

	private static final int numThreads = 10;

	private static String generateId()
	{
		var sb = new StringBuilder(idLength);
		var random = new Random();
		for (int i = 0; i < idLength; i++)
			sb.append(idChars[random.nextInt(idChars.length)]);
		return sb.toString();
	}

	private static JSONObject sendRequest(MultiValueMap<String, String> map, String api)
	{
		HttpHeaders headers = new HttpHeaders();
		RestTemplate restTemplate = new RestTemplate();
		headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

		HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(map, headers);

		ResponseEntity<String> response = restTemplate.postForEntity(api, request, String.class);

		String responseJson = response.toString();
		if (!responseJson.contains("{"))
		{
			throw new RuntimeException("Response is not good json");
		}
		responseJson = responseJson.substring(responseJson.indexOf('{'));
		responseJson = responseJson.substring(0, responseJson.lastIndexOf( '}') + 1);

		return new JSONObject(responseJson);
	}

	private static JSONObject makeSemesterRequest(SemesterYear semesterYear)
	{
		MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
		map.add("yearterm", semesterYear.getYearTerm());

		return sendRequest(map, semesterApi);
	}

	private static JSONObject makeCoursesRequest(SemesterYear semesterYear, JSONObject departments)
	{
		MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
		map.add("sessionId", generateId());
		map.add("searchObject[yearterm]", semesterYear.getYearTerm());
		for (String dept : departments.keySet())
		{
			map.add("searchObject[teaching_areas][]", dept);
		}

		return sendRequest(map, coursesApi);
	}

	private static JSONObject makeSectionRequest(SemesterYear semesterYear, CurriculumId curriculumId, TitleCode titleCode)
	{
		MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
		map.add("yearterm", semesterYear.getYearTerm());
		map.add("sessionId", generateId());
		map.add("courseId", (new CourseId(curriculumId, titleCode)).toString());

		return sendRequest(map, sectionsApi);
	}

	public static Semester getSemester(SemesterYear semesterYear)
	{
		JSONObject semesterResponse;
		System.out.printf("Requesting semester: %s\n", semesterYear.getYearTerm());
		try
		{
			semesterResponse = makeSemesterRequest(semesterYear);
		}
		catch (Exception e)
		{
			System.out.println("Exception getting semester:\n" + e.toString());
			return null;
		}

		Map<PersonId, Instructor> instructors = new HashMap<>();
		int numProfessorsCovered = 0;
		for (Object item : semesterResponse.getJSONArray("instructor_list"))
		{
			numProfessorsCovered++;
			System.out.printf("\rGetting instructor %d/%d         ", numProfessorsCovered, semesterResponse.getJSONArray("instructor_list").length());

			JSONObject instructorObj = (JSONObject) item;

			PersonId personId = new PersonId(instructorObj.getInt("id"));
			ByuId byuId = null;
			NetId netId = null;
			String firstName = instructorObj.optString("first_name");
			String lastName = instructorObj.optString("last_name");
			String sortName = instructorObj.optString("sort_name");
			String preferredFirstName = null;
			String restOfName = null;
			String surname = null;
			String phoneNumber = null;
			RmpRating rmpRating = null;

			Instructor instructor = new Instructor(
					personId,
					byuId,
					netId,
					firstName,
					lastName,
					sortName,
					preferredFirstName,
					restOfName,
					surname,
					phoneNumber,
					rmpRating
			);

			instructors.put(personId, instructor);
		}
		System.out.println();

		JSONObject coursesResponse;
		System.out.println("Requesting courses");
		try
		{
			coursesResponse = makeCoursesRequest(semesterYear, semesterResponse.getJSONObject("department_list"));
		}
		catch (Exception e)
		{
			System.out.println("Exception getting courses:\n" + e.toString());
			return null;
		}

		Map<CourseId, Course> courses = new HashMap<>();

		final Iterator<String> courseIterator = coursesResponse.keySet().iterator();
		final List<Integer> countingList = new ArrayList<>(List.of(0));
		Callable<Boolean> courseTask = () ->
		{
			countingList.set(0, countingList.get(0) + 1);
			System.out.printf("\rGetting course: %d/%d       ", countingList.get(0), coursesResponse.keySet().size());

			String courseKey = courseIterator.next();

			JSONObject courseObj = coursesResponse.getJSONObject(courseKey);

			CurriculumId curriculumId = new CurriculumId(courseObj.getInt("curriculum_id"));
			TitleCode titleCode = new TitleCode(courseObj.getInt("title_code"));
			CourseId courseId = new CourseId(curriculumId, titleCode);

			JSONObject sectionsResponse;
			try
			{
				sectionsResponse = makeSectionRequest(semesterYear, curriculumId, titleCode);
			}
			catch (Exception e)
			{
				System.out.println("Exception getting sections:\n" + e.toString());
				throw e;
			}

			JSONObject catalogObj = sectionsResponse.getJSONObject("catalog");
			JSONArray sectionsObj = sectionsResponse.getJSONArray("sections");

			String deptName = courseObj.getString("dept_name");
			int catalogNumber = courseObj.getInt("catalog_number");
			String catalogSuffix = courseObj.optString("catalog_suffix");
			String title = courseObj.optString("title");
			String fullTitle = courseObj.optString("full_title");
			double creditHours = catalogObj.optDouble("credit_hours");
			String description = catalogObj.optString("description");
			String effectiveDate = catalogObj.optString("effective_date");
			String expiredDate = catalogObj.optString("expired_date");
			String effectiveYearTerm = catalogObj.optString("effective_year_term");
			String expiredYearTerm = catalogObj.optString("expired_year_term");
			String honorsApproved = catalogObj.optString("honors_approved");
			double labHours = catalogObj.optDouble("lab_hours");
			double lectureHours = catalogObj.optDouble("lecture_hours");
			String note = catalogObj.optString("note");
			String offered = catalogObj.optString("offered");
			String prerequisite = catalogObj.optString("catalog_prereq");
			String recommended = catalogObj.optString("recommended");
			String whenTaught = catalogObj.optString("when_taught");

			List<Section> sections = new ArrayList<>();

			for (Object item1 : sectionsObj)
			{
				JSONObject sectionObj = (JSONObject) item1;

				int sectionNumber = sectionObj.getInt("section_number");
				String sectionType = sectionObj.optString("section_type");
				double minimumCreditHours = sectionObj.optDouble("minimum_credit_hours");
				String creditType = sectionObj.optString("credit_type");
				String fixedOrVariable = sectionObj.optString("fixed_or_variable");
				JSONObject availabilityObj = sectionObj.getJSONObject("availability");
				int classSize = availabilityObj.optInt("class_size");
				int seatsAvailable = availabilityObj.optInt("seats_available");
				int waitlistSize = availabilityObj.optInt("waitlist_size");
				String honors = sectionObj.optString("honors");
				String mode = sectionObj.optString("mode");
				String modeDesc = sectionObj.optString("mode_desc");
				String yearTerm = sectionObj.optString("year_term");

				List<Instructor> sectionInstructors = new ArrayList<>();

				for (Object item2 : sectionObj.getJSONArray("instructors"))
				{
					JSONObject instructorObj = (JSONObject) item2;

					PersonId personId = new PersonId(instructorObj.getInt("person_id"));

					Instructor originalInstructor = instructors.get(personId);

					ByuId byuId = new ByuId(instructorObj.getInt("byu_id"));
					NetId netId = new NetId(instructorObj.getString("net_id"));
					String firstName = originalInstructor.getFirstName();
					String lastName = originalInstructor.getLastName();
					String sortName = originalInstructor.getSortName();
					String preferredFirstName = instructorObj.optString("preferred_first_name");
					String restOfName = instructorObj.optString("rest_of_name");
					String surname = instructorObj.optString("surname");
					String phoneNumber = instructorObj.optString("phone_number");
					RmpRating rmpRating = null;

					Instructor newInstructor = new Instructor(
							personId,
							byuId,
							netId,
							firstName,
							lastName,
							sortName,
							preferredFirstName,
							restOfName,
							surname,
							phoneNumber,
							rmpRating
					);

					// not necessary to update original dict?
					// instructors.put(personId, newInstructor);
					sectionInstructors.add(newInstructor);
				}

				List<Meeting> meetings = new ArrayList<>();

				for (Object item2 : sectionObj.getJSONArray("times"))
				{
					JSONObject timeObj = (JSONObject) item2;

					int beginTime = timeObj.optInt("begin_time");
					int endTime = timeObj.optInt("end_time");
					String building = timeObj.optString("building");
					String room = timeObj.optString("room");
					boolean sun = timeObj.optString("sun").length() > 0;
					boolean mon = timeObj.optString("mon").length() > 0;
					boolean tue = timeObj.optString("tue").length() > 0;
					boolean wed = timeObj.optString("wed").length() > 0;
					boolean thu = timeObj.optString("thu").length() > 0;
					boolean fri = timeObj.optString("fri").length() > 0;
					boolean sat = timeObj.optString("sat").length() > 0;
					int sequenceNum = timeObj.optInt("sequence_number");

					meetings.add(new Meeting(
							beginTime,
							endTime,
							building,
							room,
							sun,
							mon,
							tue,
							wed,
							thu,
							fri,
							sat,
							sequenceNum
					));
				}

				sections.add(new Section(
						courseId,
						deptName,
						catalogNumber,
						catalogSuffix,
						sectionNumber,
						sectionType,
						creditHours,
						minimumCreditHours,
						creditType,
						fixedOrVariable,
						classSize,
						seatsAvailable,
						waitlistSize,
						honors,
						mode,
						modeDesc,
						yearTerm,
						sectionInstructors,
						meetings
				));
			}

			courses.put(courseId,
					new Course(
							courseId,
							deptName,
							catalogNumber,
							catalogSuffix,
							title,
							fullTitle,
							creditHours,
							description,
							effectiveDate,
							expiredDate,
							effectiveYearTerm,
							expiredYearTerm,
							honorsApproved,
							labHours,
							lectureHours,
							note,
							offered,
							prerequisite,
							recommended,
							whenTaught,
							sections
					));
			return true;
		};

		List<Callable<Boolean>> courseTasks = new ArrayList<>();
		for (int i = 0; i < coursesResponse.keySet().size(); i++)
		{
			courseTasks.add(courseTask);
		}

		ExecutorService executor = Executors.newFixedThreadPool(numThreads);
		try
		{
			executor.invokeAll(courseTasks);
		}
		catch (InterruptedException e)
		{
			e.printStackTrace();
			executor.shutdownNow();
			return null;
		}
		// will make the executor finish all its added tasks
		executor.shutdown();
		System.out.println();

		return new Semester(semesterYear, courses);
	}
}