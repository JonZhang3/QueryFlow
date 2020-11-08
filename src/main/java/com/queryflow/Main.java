package com.queryflow;

import com.alibaba.druid.sql.PagerUtils;

public class Main {

    public static void main(String[] args) {
        String sql = "SELECT g.*" +
            "    , ROUND(6378.138 * 2 * ASIN(SQRT(POW(SIN((? * PI() / 180 - t.latitude * PI() / 180) / 2), 2) + COS(? * PI() / 180) * COS(t.latitude * PI() / 180) * POW(SIN((? * PI() / 180 - t.longitude * PI() / 180) / 2), 2))), 2) AS distancecd " +
            "    , t.agentname, t.agentlogo, t.compaddress " +
            "FROM t_bas_integral_goods g " +
            "    LEFT JOIN t_bas_agent t ON g.agentid = t.AGENTID " +
            "WHERE t.AGENTTYPE = '2' " +
            "    AND t.pass = '#{1}'" +
            "    AND t.dl_type = #{2}" +
            "    AND g.type = #{3} " +
            "ORDER BY distancecd ASC";
        System.out.println(PagerUtils.count(sql, "mysql"));
    }

}
