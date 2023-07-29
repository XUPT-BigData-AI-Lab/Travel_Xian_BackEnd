package com.coach_station.bootmall.configuration;
import com.alibaba.fastjson.JSONObject;
import com.coach_station.bootmall.entity.RelationLine;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

/**
 * @Auther: yjw
 * @Date: 2022/04/26/16:56
 * @Description:
 */

public class Apriori {

    private double support;
    private double confidence;
    private final String ITEMSPLITE = ";";

    public Apriori() {
        support = 0.01;
        confidence = 0.30;

    }

    public Apriori(double support, double confidence) {
        super();
        this.support = support;
        this.confidence = confidence;
    }

    /**
     * 从src处读取所有数据，将一条条记录保存在list里
     *
     * @param src
     * @return
     * @throws IOException
     */
    public List<String> readDataFile(String src) throws IOException {
        List recordList = new ArrayList<String>();
        InputStreamReader in = new InputStreamReader(new FileInputStream(src), "UTF-8");
        BufferedReader bin = new BufferedReader(in);

        String record = "";
        while ((record = bin.readLine()) != null) {
            recordList.add(record);
        }
        return recordList;
    }

    /**
     * 对每一条记录中出现的一个item统计其出现的次数，得到频繁项1的集合
     *
     * @param recordList
     * @return
     */
    public Map<String, Integer> oneFrequentSet(List<String> recordList) {
        Map<String, Integer> freOne = new HashMap<String, Integer>();
        if (recordList != null && recordList.size() > 0) {
            for (String record : recordList) {
                String items[] = record.split(ITEMSPLITE);
                for (String item : items) {
                    item += ITEMSPLITE;
                    if (freOne.get(item) == null) {
                        freOne.put(item, 1);
                    } else {
                        freOne.put(item, freOne.get(item) + 1);
                    }
                }
            }
        }
        return freOne;
    }

    /**
     * 对项集的支持度检查，不满足支持度的剔除
     *
     * @param rawItemSets
     * @param size
     */
    public Map<String, Integer> getSupportedItemSets(Map<String, Integer> rawItemSets, int size) {
        Map<String, Integer> supportedItemSets = new HashMap<String, Integer>();
        for (Entry<String, Integer> entry : rawItemSets.entrySet()) {
            if (entry.getValue() * 1.0 > support * size) {
                supportedItemSets.put(entry.getKey(), entry.getValue());
            }
        }
        if (supportedItemSets.size() == 0) {
            System.out.println("没有满足支持度=" + support + "的频繁项集");
            return null;
        }
        return supportedItemSets;
    }

    /**
     * 由k-1项构成的频繁项集构造k项频繁项集,组合
     *
     * @param frequentSets
     * @return
     */
    public Map<String, Integer> getNextCandidate(Map<String, Integer> frequentSets) {
        Map<String, Integer> candidateFrequentSets = new HashMap<String, Integer>();
        List<String> freSets1 = mapKeyToList(frequentSets);
        List<String> freSets2 = mapKeyToList(frequentSets);
        //System.out.println(freSets1+"\n"+freSets2);
        for (int i = 0; i < freSets1.size(); i++) {
            String record1 = freSets1.get(i);
            //	System.out.println("record1:  "+record1);
            for (int j = i + 1; j < freSets2.size(); j++) {
                String record2 = freSets2.get(j);
                //	System.out.println("record2:  "+record2);
                String m = merge(record1, record2);
                //	System.out.println("m:  "+m);
                if (!containsFreuentSet(candidateFrequentSets, m)) {
                    candidateFrequentSets.put(m, 0);
                }
                ;
            }
        }
        return candidateFrequentSets;
    }

    /**
     * 判断频繁项集freSets里是否完全含有一个给定的项集m
     *
     * @param freSets
     * @param m
     * @return
     */
    public boolean containsFreuentSet(List<String> freSets, String m) {

        boolean flag = false;
        for (String record : freSets) {
            if (record.length() >= m.length()) {
                String[] items = m.split(ITEMSPLITE);
                for (int i = 0; i < items.length; i++) {
                    if (record.indexOf(items[i]) == -1) {
                        flag = false;
                        break;
                    } else {
                        flag = true;
                    }
                }
                if (flag == true) {
                    break;
                }
            }
        }
        return flag;
    }

    /**
     * 判断频繁项集freSets里是否含有一个给定的项集m
     *
     * @param map
     * @param m
     * @return
     */
    public boolean containsFreuentSet(Map<String, Integer> map, String m) {
        List<String> list = mapKeyToList(map);
        return containsFreuentSet(list, m);
    }

    /**
     * 将map表示的频繁项集用List表示，便于找到下标处理
     *
     * @param map
     * @return
     */
    public List<String> mapKeyToList(Map<String, Integer> map) {
        List<String> list = new ArrayList<String>();
        if (map.size() != 0) {
            Set<String> set = map.keySet();
            for (String s : set) {
                list.add(s);
            }
        }
        return list;
    }

    /**
     * 对两个项集record1和record2进行合并，去除其中重复的item项目
     *
     * @param record1
     * @param record2
     * @return
     */
    public String merge(String record1, String record2) {
        String items2[] = record2.split(ITEMSPLITE);
        String record = record1;
        for (int i = 0; i <= items2.length - 1; i++) {
            String item = items2[i];
            if (record1.indexOf(item) == -1) {
                record += item + ITEMSPLITE;
            }
        }
        return record;
    }

    /**
     * 统计组合的频繁项集的支持度
     *
     * @param map
     * @param data
     */
    public void countCandidateFreqSet(Map<String, Integer> map, List<String> data) {
        int times;
        for (Entry<String, Integer> entry : map.entrySet()) {
            String str = entry.getKey();
            times = count(data, str);
            entry.setValue(times);
        }

    }

    /**
     * 求初始data里项集m的出现的次数，即m的支持度
     *
     * @param data
     * @param m
     * @return
     */
    public int count(List<String> data, String m) {
        boolean flag;
        int count = 0;
        for (String record : data) {
            flag = true;
            if (record.length() >= m.length()) {
                String items[] = m.split(ITEMSPLITE);
                for (int i = 0; i < items.length; i++) {
                    if (record.indexOf(items[i]) == -1) {
                        flag = false;
                        break;
                    }
                }
                if (flag == true) {
                    count++;
                }
            }
        }
        return count;
    }

    public Map<String, Integer> apriori(List<String> srcData) throws IOException {
        Map<String, Integer> freSets = new HashMap<String, Integer>();
        Map<String, Integer> candidateFreSets = new HashMap<String, Integer>();

        int len = srcData.size();

        Map<String, Integer> oneFreSets = oneFrequentSet(srcData);//获取项集为1的集合
		/*System.out.println("--------------------初始频繁1项项集-----------------------------");
		for(Entry<String, Integer> entry:oneFreSets.entrySet()){
			System.out.println(entry.getKey()+":"+entry.getValue());
		}*/

        oneFreSets = getSupportedItemSets(oneFreSets, len);//第一次进行清理不符合支持度的项集
		/*System.out.println("--------------------初始频繁1项项集支持度检查-----------------------------");
		for(Entry<String, Integer> entry:oneFreSets.entrySet()){
			System.out.println(entry.getKey()+":"+entry.getValue());
		}*/
        int i = 1;
        while (oneFreSets != null && oneFreSets.size() > 0) {
            freSets = union(freSets, oneFreSets);          //合并上一次所求满足支持度的项集，会不会重复？

            //由k项项集找k+1项项集
		/*	System.out.println("--------------------初始频繁项集第"+i+"次合并的结果-----------------------------");
			for(Entry<String, Integer> entry:freSets.entrySet()){
				System.out.println(entry.getKey()+":"+entry.getValue());
			}*/
            i++;
            candidateFreSets = getNextCandidate(oneFreSets);
			/*System.out.println("--------------------组合的"+i+"项结果-----------------------------");
			for(Entry<String, Integer> entry:candidateFreSets.entrySet()){
				System.out.println(entry.getKey()+":"+entry.getValue());
			}*/
            //对候选组合的k+1项项集进行统计
            countCandidateFreqSet(candidateFreSets, srcData);
		/*	System.out.println("--------------------组合支持度计算结果-----------------------------");
			for(Entry<String, Integer> entry:candidateFreSets.entrySet()){
				System.out.println(entry.getKey()+":"+entry.getValue());
			}
			*/
            //筛选满足支持度的项集，赋值给oneFreSets
            oneFreSets = getSupportedItemSets(candidateFreSets, len);
			/*if(oneFreSets!=null){
				System.out.println("--------------------频繁项集（"+i+"项）支持度筛选的结果-----------------------------");
				for(Entry<String, Integer> entry:oneFreSets.entrySet()){
					System.out.println(entry.getKey()+":"+entry.getValue());
				}
			}*/
        }

        return freSets;
    }

    /**
     * 对所有满足支持度的频繁项集做一次冗余清理：频繁项集的子集也是频繁项集，项集为1的频繁项集也没有意义
     *
     * @param freSets
     * @param oneFreSets
     */
    public Map<String, Integer> union(Map<String, Integer> freSets, Map<String, Integer> oneFreSets) {
        Map<String, Integer> tmp = new HashMap<String, Integer>();
        for (Entry<String, Integer> entry : freSets.entrySet()) {
            tmp.put(entry.getKey(), entry.getValue());
        }

        for (Entry<String, Integer> entry : freSets.entrySet()) {
            String str = entry.getKey();
            if (containsFreuentSet(oneFreSets, str)) {
                tmp.remove(str);
            }
        }

        tmp.putAll(oneFreSets);
        return tmp;


    }

    /**
     * 求关联规则
     *
     * @param freSets：频繁项集
     * @param data：源数据
     * @return
     */
    public Map<String, RelationLine> getRelation(Map<String, Integer> freSets, List<String> data) {
        Map<String, RelationLine> results = new HashMap<String, RelationLine>();   //规则结果：（关联规则，置信度）
        List<String> freKeys = mapKeyToList(freSets);          //频繁项集
        System.out.println(freKeys);
        List<String> subSets = new ArrayList<String>();
        String front = "", end = "";
        for (String freRecord : freKeys) {
            System.out.println("\n-------------------------频繁项集------------------\n" + freRecord);
            subSets = getSubSets(freRecord);  //每个频繁项集求其真子集
            System.out.println("------------------真子集-----------------\n" + subSets);
            for (int i = 0; i < subSets.size(); i++) {
                front = subSets.get(i);
                System.out.println("前置：" + front + "^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^");
                for (int j = 0; j < subSets.size(); j++) {
                    end = subSets.get(j);
                    if (!(isIntersected(front, end))) {  //前置条件和后置条件不可以有交集
                        double confidenceTest = getRealConfidence(data, subSets.get(i), subSets.get(j));
                        System.out.println("前置--->后置：" + front + "--->" + end + " : " + confidenceTest);
                        if (confidenceTest > confidence) {
                            System.out.println("put:(" + front + "," + end + ")");
                            RelationLine relationLine = new RelationLine();
                            relationLine.setFrontLine(subSets.get(i));
                            relationLine.setEndLine(subSets.get(j));
                            relationLine.setConfidence(BigDecimal.valueOf(confidenceTest));
                            results.put(subSets.get(i) + "--->" + subSets.get(j), relationLine);
                        }
                    }
                }
            }
        }
        System.out.println("asd");
        return results;
    }

    /**
     * 对频繁项集的每一条规则 freRecordFront-->freRecordEnd,求置信度
     *
     * @param data
     * @param freRecordFront
     * @param freRecordEnd
     * @return
     */
    public double getRealConfidence(List<String> data, String freRecordFront, String freRecordEnd) {
        String str = freRecordFront + freRecordEnd;
        return count(data, str) * 1.0 / count(data, freRecordFront);
    }
    /**
     *
     * 对频繁项集的真子集，根据源数据data,统计每一个子集的支持度
     * @param subSets
     * @param data
     * @return
     *//*
	public Map<String, Integer> getSubSetSupport(List<String> subSets,List<String> data) {
		Map<String, Integer> map=new HashMap<String, Integer>();
		if(subSets==null||subSets.size()==0)
			return null;
		for(int i=0;i<subSets.size();i++){
			map.put(subSets.get(i), count(data, subSets.get(i)));
		}
		return map;
	}*/

    /**
     * 对一项频繁项集求其真子集
     *
     * @param str
     * @return
     */
    public List<String> getSubSets(String str) {
        List<String> subSets = new ArrayList<String>();
        String[] items = str.split(ITEMSPLITE);
        int len = items.length;
        String item = "";
        String flag = "";
        for (int i = 1; i < (int) Math.pow(2, len) - 1; i++) {
            flag = "";
            item = "";
            int tmp = i;
            do {
                flag += "" + tmp % 2;
                tmp /= 2;
            } while (tmp > 0);

            for (int j = 0; j < flag.length(); j++) {
                if (flag.charAt(j) == '1') {
                    item += items[j] + ITEMSPLITE;
                }
            }
            subSets.add(item);
        }
        return subSets;
    }

    /**
     * 比较两个项目是否有交集
     *
     * @param front
     * @param end
     * @return
     */
    public boolean isIntersected(String front, String end) {
        if (front == null || front.length() == 0 || end == null || end.length() == 0) {
            return false;
        }
        String[] frontItems = front.split(ITEMSPLITE);
        String[] endItems = end.split(ITEMSPLITE);
        boolean flag = false;
        for (int i = 0; i < frontItems.length; i++) {
            for (int j = 0; j < endItems.length; j++) {
                if (frontItems[i].equals(endItems[j])) {
                    flag = true;
                    break;
                }
            }
        }
        return flag;
    }

    public static List<RelationLine> getRelationLines(List<String> srcData) throws IOException {
        Apriori apriori = new Apriori();
//        List<String> srcData = apriori.readDataFile("E:/data.txt");
		/*源数据
		 * 记录条数：9
			1;2;5;
			2;4;
			2;3;
			1;2;4;
			1;3;
			2;3;
			1;3;
			1;2;3;5;
			1;2;3;

		 */
        System.out.println(JSONObject.toJSONString(srcData));
        Map<String, Integer> map = apriori.apriori(srcData);
        for (Entry<String, Integer> entry : map.entrySet()) {
            System.out.println(entry.getKey() + ":" + entry.getValue());
        }
		/*频繁项集
		 * 2;1;5;:2
		2;1;3;:2
		4;2;:2
		*/
        System.out.println("**********************************************");
        Map<String, RelationLine> results = apriori.getRelation(map, srcData);
        ArrayList<RelationLine> relationLines = new ArrayList<>();
        for (Entry<String, RelationLine> entry : results.entrySet()) {
            relationLines.add(entry.getValue());
        }
//        for (Entry<String, Double> entry : resuults.entrySet()) {
//            System.out.println(entry.getKey() + ":" + entry.getValue());
//        }
		/*关联规则
		4; ---> 2;:1.0
		5; ---> 2;:1.0
		1;5; ---> 2;:1.0
		5; ---> 1;:1.0
		2;5; ---> 1;:1.0
		5; ---> 2;1;:1.0
		*/
        return relationLines;
    }

}

