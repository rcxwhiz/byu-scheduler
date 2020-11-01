package api;

import model.Semester;
import model.SemesterYear;
import model.id.CourseId;
import model.id.CurriculumId;
import model.id.TitleCode;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.Random;

public class ByuApi
{
	private static final String semesterApi = "http://saasta.byu.edu/noauth/classSchedule/ajax/getYeartermData.php";
	private static final String coursesApi = "http://saasta.byu.edu/noauth/classSchedule/ajax/getClasses.php";
	private static final String sectionsApi = "http://saasta.byu.edu/noauth/classSchedule/ajax/getSections.php";
	private static final char[] idChars = "ABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890".toCharArray();
	private static final int idLength = 20;

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

		// TODO some of the responses are potentially arrays?
		String responseJson = response.toString();
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
		// TODO need to figure out how this should work
		map.add("searchObject[teaching_areas][]", departments.keySet().toString());

		return sendRequest(map, coursesApi);
	}

	private static JSONObject makeSectionRequest(CurriculumId curriculumId, TitleCode titleCode)
	{
		MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
		map.add("sessionId", generateId());
		map.add("courseId", curriculumId.toString() + '-' + titleCode.toString());

		return sendRequest(map, semesterApi);
	}

	public static Semester getSemester(SemesterYear semesterYear)
	{
		JSONObject semesterResponse;
		try
		{
			semesterResponse = makeSemesterRequest(semesterYear);
		}
		catch (Exception e)
		{
			System.out.println("Exception getting semester:\n" + e.toString());
			return null;
		}

		JSONObject coursesResponse;
		try
		{
			coursesResponse = makeCoursesRequest(semesterYear, semesterResponse.getJSONObject("department_list"));
		}
		catch (Exception e)
		{
			System.out.println("Exception getting courses:\n" + e.toString());
			return null;
		}

		return null;
	}
}