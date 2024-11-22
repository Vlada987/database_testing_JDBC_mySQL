package sqlOperations;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

import com.github.javafaker.Faker;

public class MyUtil {

	static Faker faker = new Faker();

	public static <T> Optional<T> findUnique(List<String> list) {

		return (Optional<T>) list.stream().collect(Collectors.groupingBy(Function.identity(), Collectors.counting()))
				.entrySet().stream().filter(entry -> entry.getValue() == 1)
				.map(Map.Entry::getKey).findFirst();
	}

	public static List<List<Object>> getResultSetAsList(ResultSet resultSet) throws SQLException {

		List<List<Object>> resultList = new ArrayList<>();
		ResultSetMetaData metaData = resultSet.getMetaData();
		int columnCount = metaData.getColumnCount();

		while (resultSet.next()) {
			List<Object> row = new ArrayList<>();

			for (int i = 1; i <= columnCount; i++) {
				row.add(resultSet.getObject(i));
			}
			resultList.add(row);
		}

		return resultList;
	}

	public static boolean checkNotNull(List<List<Object>> listOfLists) {

		return listOfLists.stream().allMatch(innerList -> innerList.stream().allMatch(element -> element != null));
	}

	public static boolean hasNoDuplicates(List<String> list) {

		Set<String> set = list.stream().collect(Collectors.toSet());
		return set.size() == list.size();
	}

	public static List<List<String>> createUsers(int number) {

		List<List<String>> data = new ArrayList<>();

		for (int a = 0; a <= number; a++) {
			data.add(Arrays.asList(faker.name().firstName(), faker.name().username(), faker.internet().emailAddress(),
					faker.address().cityName()));
		}
		;
		return data;
	}

	public static String randomUsername() {
		return faker.name().username();
	}

}
