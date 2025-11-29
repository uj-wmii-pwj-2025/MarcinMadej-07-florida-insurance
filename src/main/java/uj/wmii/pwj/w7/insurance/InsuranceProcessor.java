package uj.wmii.pwj.w7.insurance;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

public class InsuranceProcessor {
    public static class InsuranceEntry {

        public int policyId;                 // policyID
        public String stateCode;             // statecode
        public String county;                // county

        public double eqSiteLimit;           // eq_site_limit
        public double huSiteLimit;           // hu_site_limit
        public double flSiteLimit;           // fl_site_limit
        public double frSiteLimit;           // fr_site_limit

        public double tiv2011;               // tiv_2011
        public double tiv2012;               // tiv_2012

        public double eqSiteDeductible;      // eq_site_deductible
        public double huSiteDeductible;      // hu_site_deductible
        public double flSiteDeductible;      // fl_site_deductible
        public double frSiteDeductible;      // fr_site_deductible

        public double pointLatitude;         // point_latitude
        public double pointLongitude;        // point_longitude

        public String line;                  // line
        public String construction;          // construction

        public int pointGranularity;         // point_granularity

        public double tivDelta;

    }

    public static List<InsuranceEntry> loadZip (String fileName) throws IOException {

        ZipFile zipFile = new ZipFile(fileName);

        ZipEntry csvFile = zipFile.stream()
        .filter(e -> e.getName().toLowerCase().endsWith(".csv"))
        .findFirst().orElseThrow(() -> new FileNotFoundException("Could not find .csv file in .zip file"));

        BufferedReader csvReader = new BufferedReader(
                new InputStreamReader(zipFile.getInputStream(csvFile),
                StandardCharsets.UTF_8));

        return csvReader.lines().skip(1).map(InsuranceProcessor::parseCsvLine).toList();


    }

    private static InsuranceEntry parseCsvLine(String line) {
        String[] p = line.split(",", -1);

        InsuranceEntry e = new InsuranceEntry();

        e.policyId = parseInt(p[0]);
        e.stateCode = p[1];
        e.county = p[2];

        e.eqSiteLimit = parseDouble(p[3]);
        e.huSiteLimit = parseDouble(p[4]);
        e.flSiteLimit = parseDouble(p[5]);
        e.frSiteLimit = parseDouble(p[6]);

        e.tiv2011 = parseDouble(p[7]);
        e.tiv2012 = parseDouble(p[8]);

        e.eqSiteDeductible = parseDouble(p[9]);
        e.huSiteDeductible = parseDouble(p[10]);
        e.flSiteDeductible = parseDouble(p[11]);
        e.frSiteDeductible = parseDouble(p[12]);

        e.pointLatitude = parseDouble(p[13]);
        e.pointLongitude = parseDouble(p[14]);

        e.line = p[15];
        e.construction = p[16];

        e.pointGranularity = parseInt(p[17]);

        e.tivDelta = e.tiv2012-e.tiv2011;

        return e;
    }

    private static double parseDouble(String s) {
        if (s == null || s.isEmpty()) return 0.0;
        return Double.parseDouble(s);
    }

    private static int parseInt(String s) {
        if (s == null || s.isEmpty()) return 0;
        return Integer.parseInt(s);
    }


}
