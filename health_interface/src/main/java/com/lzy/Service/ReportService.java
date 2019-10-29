package com.lzy.Service;

import java.util.HashMap;
import java.util.Map;

public interface ReportService {

    HashMap<String,Object> getReportData() throws Exception;

    HashMap<String, Object> getMemberInfo() throws Exception;

    HashMap<String, Object> getMemberAgeInfo(Map<String, String> ageMap);
}
