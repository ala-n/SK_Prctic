package labs.zubovich.dbutil;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TableParser {

	public static Map<Integer, List<Integer>> readTable(File file) {
		Map<Integer, List<Integer>> result = new HashMap<>();
		try (BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(file)))) {
			String line = "";
			while ((line = br.readLine()) != null) {
				String[] nums = line.split("[\\s ]+");
				List<Integer> intList = new ArrayList<>();
				intList.add(Integer.parseInt(nums[1]));
				intList.add(Integer.parseInt(nums[2]));
				intList.add(Integer.parseInt(nums[3]));
				result.put(Integer.parseInt(nums[0]), intList);
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return result;
	}

}