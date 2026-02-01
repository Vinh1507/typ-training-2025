package com.oj_cpp.core.application.service.file;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.oj_cpp.core.application.dto.file.ParsedProblemData;
import com.oj_cpp.core.application.dto.request.CreateProblemRequest;
import com.oj_cpp.core.domain.enums.ProblemLevelEnum;
import com.oj_cpp.core.domain.model.TestCase;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public class PolygonZipParser {

    private static final ObjectMapper objectMapper = new ObjectMapper();

    public static ParsedProblemData parse(InputStream inputStream) throws Exception {
        Map<String, byte[]> files = new HashMap<>();
        
        try (ZipInputStream zis = new ZipInputStream(inputStream)) {
            ZipEntry zipEntry;
            while ((zipEntry = zis.getNextEntry()) != null) {
                if (!zipEntry.isDirectory()) {
                    ByteArrayOutputStream buffer = new ByteArrayOutputStream();
                    byte[] data = new byte[1024];
                    int nRead;
                    while ((nRead = zis.read(data, 0, data.length)) != -1) {
                        buffer.write(data, 0, nRead);
                    }
                    files.put(zipEntry.getName(), buffer.toByteArray());
                }
            }
        }

        if (!files.containsKey("problem.xml")) {
            throw new IllegalArgumentException("Invalid zip: problem.xml not found");
        }
        CreateProblemRequest.CreateProblemRequestBuilder requestBuilder = CreateProblemRequest.builder();
        parseProblemXml(files.get("problem.xml"), requestBuilder);

        String propertyPath = "statements/vietnamese/problem-properties.json";
        if (!files.containsKey(propertyPath)) {
            propertyPath = files.keySet().stream()
                    .filter(k -> k.endsWith("problem-properties.json"))
                    .findFirst()
                    .orElse(null);
        }

        if (propertyPath != null) {
            parseProblemProperties(files.get(propertyPath), requestBuilder);
        } else {
            requestBuilder.content("No description available.");
        }

        if (requestBuilder.build().getLevel() == null) {
            requestBuilder.level(ProblemLevelEnum.MEDIUM);
        }

        List<TestCase> testCases = extractTestCases(files);

        return ParsedProblemData.builder()
                .request(requestBuilder.build())
                .testCases(testCases)
                .build();
    }

    private static void parseProblemXml(byte[] xmlData, CreateProblemRequest.CreateProblemRequestBuilder builder) throws Exception {
        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
        Document doc = dBuilder.parse(new ByteArrayInputStream(xmlData));
        doc.getDocumentElement().normalize();

        Element root = doc.getDocumentElement();
        String shortName = root.getAttribute("short-name");


        NodeList names = doc.getElementsByTagName("name");
        for (int i = 0; i < names.getLength(); i++) {
            Element nameEl = (Element) names.item(i);
            if ("vietnamese".equals(nameEl.getAttribute("language"))) {
                builder.title(nameEl.getAttribute("value"));
                break;
            }
        }
        if (builder.build().getTitle() == null && names.getLength() > 0) {
            builder.title(((Element) names.item(0)).getAttribute("value"));
        }

        NodeList timeLimits = doc.getElementsByTagName("time-limit");
        if (timeLimits.getLength() > 0) {
            String val = timeLimits.item(0).getTextContent();
            builder.timeLimit(Integer.parseInt(val));
        }

        NodeList memoryLimits = doc.getElementsByTagName("memory-limit");
        if (memoryLimits.getLength() > 0) {
            String val = memoryLimits.item(0).getTextContent();
            long bytes = Long.parseLong(val);
            builder.memoryLimit((int) (bytes / (1024 * 1024)));
        }
    }

    private static void parseProblemProperties(byte[] jsonData, CreateProblemRequest.CreateProblemRequestBuilder builder) throws IOException {
        JsonNode root = objectMapper.readTree(jsonData);

        String name = root.path("name").asText(null);
        if (builder.build().getTitle() == null && name != null) {
            builder.title(name);
        }

        StringBuilder content = new StringBuilder();
        appendSection(content, "", root.path("legend").asText(""));
        appendSection(content, "**Input**", root.path("input").asText(""));
        appendSection(content, "**Output**", root.path("output").asText(""));
        appendSection(content, "**Note**", root.path("notes").asText(""));

        builder.content(content.toString().trim());
    }

    private static void appendSection(StringBuilder content, String header, String text) {
        if (text != null && !text.isEmpty()) {
            if (!header.isEmpty()) {
                content.append(header).append("\n");
            }
            content.append(text).append("\n\n");
        }
    }

    private static List<TestCase> extractTestCases(Map<String, byte[]> files) {
        List<TestCase> testCases = new ArrayList<>();
        List<String> sortedKeys = new ArrayList<>(files.keySet());
        Collections.sort(sortedKeys);
        
        int order = 1;
        for (String key : sortedKeys) {
            if (key.startsWith("tests/") && !key.endsWith(".a") && !key.contains(".")) {
                String outputKey = key + ".a";
                if (files.containsKey(outputKey)) {
                    String inputContent = new String(files.get(key), StandardCharsets.UTF_8);
                    String outputContent = new String(files.get(outputKey), StandardCharsets.UTF_8);
                    
                    testCases.add(TestCase.builder()
                            .input(inputContent)
                            .expectedOutput(outputContent)
                            .isHidden(false)
                            .orderIndex(order++)
                            .build());
                }
            }
        }
        
        return testCases;
    }
}
