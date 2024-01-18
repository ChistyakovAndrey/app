package com.spandev.app.model;

import com.spandev.app.repositories.UserRepository;
import com.spandev.app.service.UserService;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import lombok.Data;
import org.springframework.stereotype.Component;

@Component
@Data
public class WeightChartData {

    private final UserService userService;

    private final UserRepository userRepository;

    private static final String GREEN_INFO_TEXT = "#26D52F";

    private static final String RED_INFO_TEXT = "#F65D57";

    private static final String NEUTRAL_INFO_TEXT = "#CDCEE8";

    public WeightChartData(UserService userService, UserRepository userRepository) {
        this.userService = userService;
        this.userRepository = userRepository;
    }

    private List<Double> getUserWeights(User user, String scaleDate) {
        List<Map<String, Object>> userWeights = userService.getWeightList(user.getId(), scaleDate);
        List<Double> doubles = new ArrayList<>();
        for (Map<String, Object> m : userWeights) {
            doubles.add((Double) m.get("weight"));
        }
        return doubles;
    }

    private Map<String, Double> getMinMaxForYAxis(List<Double> doubles) {
        Map<String, Double> minMax = new HashMap<>();
        double min = doubles.stream().min(Double::compareTo).orElse(50d);
        double max = doubles.stream().max(Double::compareTo).orElse(100d);

        double miny = 0;
        if (min % 5 != 0) {
            while (miny < min) {
                miny += 5;
            }
            miny -= 5;
        } else {
            miny = min;
        }
        minMax.put("min", miny);

        double maxy = 0;
        if (max % 5 != 0) {
            while (maxy < max) {
                maxy += 5;
            }
        } else {
            maxy = max;
        }
        minMax.put("max", maxy);
        return minMax;
    }

    private long getDaysFromBeginning(String beginDate, String targetDate) {
        LocalDate begin = LocalDate.parse(beginDate);
        LocalDate target = LocalDate.parse(targetDate);
        return ChronoUnit.DAYS.between(begin, target);
    }

    private String getGoalWithCurrentProgress(String goalWeight, double avgPerDay, double todayWeight, String todayDate, boolean begin) {
        if (begin) {
            return "Недостаточно данных";
        }
        int days = 0;
        if (avgPerDay >= 0) {
            return "Никогда";
        }
        while (todayWeight > Double.parseDouble(goalWeight)) {
            todayWeight += avgPerDay;
            days++;
        }
        LocalDate currentDate = LocalDate.parse(todayDate);
        LocalDate goalDate = currentDate.plusDays(days);
        return goalDate.toString();
    }

    public String getScaleDate(String scale) {
        long scaleDays =
                scale != null
                        ? scale.equals("two_weeks")
                        ? 13
                        : scale.equals("month")
                        ? 29
                        : scale.equals("week")
                        ? 6
                        : 89
                        : 89;
        return LocalDate.now().minusDays(scaleDays).toString();
    }

    private String getGoalReachDate(int idx, int days, String goal, List<Map<String, Object>> userAllWeights, List<Map<String, Object>> userWeights, double todayWeight, String today) {
        boolean begin = userWeights.size() == userAllWeights.size() && idx == 0;
        LocalDate currentDay = LocalDate.parse(today);
        int listSizeDifference = userAllWeights.size() - userWeights.size();
        if (idx >= days) {
            double progressDay = ((double) userWeights.get(idx).get("weight") - (double) userWeights.get(idx - days).get("weight")) / days;
            return getGoalWithCurrentProgress(goal, progressDay, todayWeight, currentDay.toString(), begin);
        } else if (listSizeDifference - days + idx >= 0) {
            int rest = days - idx;
            int index = listSizeDifference - rest;
            double progressDay = ((double) userWeights.get(idx).get("weight") - (double) userAllWeights.get(index).get("weight")) / days;
            return getGoalWithCurrentProgress(goal, progressDay, todayWeight, currentDay.toString(), begin);
        }
        return "Недостаточно данных";
    }

    private String getAvgProgress(int idx, int days, List<Map<String, Object>> userAllWeights, List<Map<String, Object>> userWeights) {
        int listSizeDifference = userAllWeights.size() - userWeights.size();
        if (idx >= days) {
            double progressDay = ((double) userWeights.get(idx).get("weight") - (double) userWeights.get(idx - days).get("weight")) / days;
            return progressDay > 0
                    ? "+" + String.format("%.3f", Math.abs(progressDay))
                    : progressDay < 0
                    ? "-" + String.format("%.3f", Math.abs(progressDay))
                    : "0";
        } else if (listSizeDifference - days + idx >= 0) {
            int rest = days - idx;
            int index = listSizeDifference - rest;
            double progressDay = ((double) userWeights.get(idx).get("weight") - (double) userAllWeights.get(index).get("weight")) / days;
            return progressDay > 0
                    ? "+" + String.format("%.3f", Math.abs(progressDay))
                    : progressDay < 0
                    ? "-" + String.format("%.3f", Math.abs(progressDay))
                    : "0";
        }
        return "Недостаточно данных";
    }

    private String getProgress(int days, int idx, double weight, List<Map<String, Object>> userWeights, List<Map<String, Object>> userAllWeights, Map<String, Object> column) {
        int listSizeDifference = userAllWeights.size() - userWeights.size();
        if (idx >= days) {
            double dif = (double) userWeights.get(idx).get("weight") - (double) userWeights.get(idx - days).get("weight");
            column.put("dataColor", dif > 0 ? RED_INFO_TEXT : GREEN_INFO_TEXT);
            return dif > 0 ? "+" + String.format("%.3f", Math.abs(dif)) : "-" + String.format("%.3f", Math.abs(dif));
        } else if (listSizeDifference - days + idx >= 0) {
            double dif = (double) userWeights.get(idx).get("weight") - (double) userAllWeights.get(listSizeDifference - days + idx).get("weight");
            column.put("dataColor", dif > 0 ? RED_INFO_TEXT : GREEN_INFO_TEXT);
            return dif > 0 ? "+" + String.format("%.3f", Math.abs(dif)) : "-" + String.format("%.3f", Math.abs(dif));
        }
        return "Недостаточно данных";
    }

    public List<Map<String, Object>> getXdata(User user, Integer width, String scale) {
        String scaleDate = getScaleDate(scale);
        List<Map<String, Object>> userWeights = userService.getWeightList(user.getId(), scaleDate);
        List<Map<String, Object>> userAllWeights = userService.getWeightList(user.getId());
        List<Map<String, String>> goals = userRepository.weightInit(user.getId());
        List<Double> doubles = getUserWeights(user, scaleDate);
        int chartBlockWidth = (int) (width * 0.8 - 140);
        // вычисляем ширину колонки, учитывая пробелы - marginRight
        int marginRight = userWeights.size() > 25
                ? 2
                : userWeights.size() > 10
                ? 5
                : 10;
        int columnWidth = (chartBlockWidth - (doubles.size() - 1) * marginRight) / doubles.size();
        List<Map<String, Object>> data = new ArrayList<>();
        Map<String, Double> minMax = getMinMaxForYAxis(doubles);
        for (int i = 0; i < userWeights.size(); i++) {
            double percent = (minMax.get("max") - minMax.get("min")) / 100;
            Map<String, Object> column = new HashMap<>();
            column.put("margin", marginRight);
            column.put("width", columnWidth - 2); // 2 - border 1px с каждой стороны
            column.put("weight", userWeights.get(i).get("weight"));

            int listsSizeDifference = userAllWeights.size() - userWeights.size();
            double difference =
                    listsSizeDifference == 0
                            ? i > 0
                            ? (double) userWeights.get(i - 1).get("weight") - (double) userWeights.get(i).get("weight")
                            : 0d
                            : i > 0
                            ? (double) userWeights.get(i - 1).get("weight") - (double) userWeights.get(i).get("weight")
                            : (double) userAllWeights.get(listsSizeDifference - 1).get("weight") - (double) userWeights.get(i).get("weight");

            String progressDay = difference > 0 ? "-" + String.format("%.3f", Math.abs(difference)) : "+" + String.format("%.3f", Math.abs(difference));
            column.put("progressDay", progressDay);

            double commonDifference = (double) userWeights.get(i).get("weight") - (double) userAllWeights.get(0).get("weight");
            String commonProgress = commonDifference != 0
                    ? commonDifference > 0
                    ? "+" + String.format("%.3f", Math.abs(commonDifference))
                    : "-" + String.format("%.3f", Math.abs(commonDifference))
                    : "0";
            column.put("commonProgress", commonProgress);

            int daysFromBeginning =
                    (int) getDaysFromBeginning(String.valueOf(userAllWeights.get(0).get("date")), String.valueOf(userWeights.get(i).get("date")));
            double avgProgressPerDay = daysFromBeginning > 0 ? commonDifference / daysFromBeginning : 0;
            String avgPerDay = avgProgressPerDay != 0
                    ? avgProgressPerDay > 0
                    ? "+" + String.format("%.3f", avgProgressPerDay)
                    : "-" + String.format("%.3f", Math.abs(avgProgressPerDay))
                    : "0";
            column.put("avgProgressPerDay", avgPerDay);


            boolean begin = userWeights.size() == userAllWeights.size() && i == 0;
            String goalWithCurrentProgress =
                    getGoalWithCurrentProgress(goals.get(0).get("g_weight"),
                            avgProgressPerDay,
                            (double) userWeights.get(i).get("weight"),
                            userWeights.get(i).get("date").toString(),
                            begin);
            column.put("goalWithCurrentProgress", goalWithCurrentProgress);

            double percents = 0;
            while ((double) userWeights.get(i).get("weight") > (minMax.get("min") + percent)) {
                percents++;
                percent += (minMax.get("max") - minMax.get("min")) / 100;
            }
            /*
                3.4 = типа один процент от высоты блока. Пока так
                + 7 это на случай если минимальный вес точно соответствует минимальному значению на оси У -
                чтобы хоть что-то показывалось - эти самые 7px
             */
            double height = percents * 3.4 + 7;
            column.put("height", height);

            /* красим колонку разным цветом в зависимости от разницы с предыдущим днем
               вес прибавился - красим красным, иначе зеленым
               если это первый день, то по-любому зеленым */
            String color =
                    listsSizeDifference > 0
                            ? i == 0
                            ? (double) userAllWeights.get(listsSizeDifference - 1).get("weight") > (double) userWeights.get(i).get("weight")
                            ? "chart_green"
                            : "chart_red"
                            : (double) userWeights.get(i - 1).get("weight") > (double) userWeights.get(i).get("weight")
                            ? "chart_green"
                            : "chart_red"
                            : i > 0
                            ? (double) userWeights.get(i - 1).get("weight") < (double) userWeights.get(i).get("weight")
                            ? "chart_red"
                            : "chart_green"
                            : "chart_green";
            column.put("chart_class", color);

            // каждую колонку двигаем вправо на нужное место
            column.put("left", (columnWidth + marginRight) * i);

            column.put("date", userWeights.get(i).get("date"));

            double weight = (double) userWeights.get(i).get("weight");
            String today = userWeights.get(i).get("date").toString();
            column.put("goalWithCurrent3DaysProgress", getGoalReachDate(i, 3, goals.get(0).get("g_weight"), userAllWeights, userWeights, weight, today));
            column.put("goalWithCurrentWeekProgress", getGoalReachDate(i, 7, goals.get(0).get("g_weight"), userAllWeights, userWeights, weight, today));
            column.put("goalWithCurrentMonthProgress", getGoalReachDate(i, 30, goals.get(0).get("g_weight"), userAllWeights, userWeights, weight, today));

            column.put("avg3DaysProgress", getAvgProgress(i, 3, userAllWeights, userWeights));
            column.put("avgWeekProgress", getAvgProgress(i, 7, userAllWeights, userWeights));
            column.put("avgMonthProgress", getAvgProgress(i, 30, userAllWeights, userWeights));

            char apd = avgPerDay.charAt(0);
            column.put("progressPerDayColor", apd == '+' ? RED_INFO_TEXT : apd == '-' ? GREEN_INFO_TEXT : NEUTRAL_INFO_TEXT);

            column.put("oneDayProgressColor", difference > 0 ? GREEN_INFO_TEXT : difference < 0 ? RED_INFO_TEXT : NEUTRAL_INFO_TEXT);

            char gap3 = getAvgProgress(i, 3, userAllWeights, userWeights).charAt(0);
            column.put("avg3DaysProgressColor", gap3 == '+' ? RED_INFO_TEXT : gap3 == '-' ? GREEN_INFO_TEXT : NEUTRAL_INFO_TEXT);

            char gap7 = getAvgProgress(i, 7, userAllWeights, userWeights).charAt(0);
            column.put("avgWeekProgressColor", gap7 == '+' ? RED_INFO_TEXT : gap7 == '-' ? GREEN_INFO_TEXT : NEUTRAL_INFO_TEXT);

            char gap30 = getAvgProgress(i, 30, userAllWeights, userWeights).charAt(0);
            column.put("avgMonthProgressColor", gap30 == '+' ? RED_INFO_TEXT : gap30 == '-' ? GREEN_INFO_TEXT : NEUTRAL_INFO_TEXT);

            char c3 = getProgress(3, i, weight, userWeights, userAllWeights, column).charAt(0);
            column.put("progress3DaysColor", c3 == '+' ? RED_INFO_TEXT : c3 == '-' ? GREEN_INFO_TEXT : NEUTRAL_INFO_TEXT);

            char c7 = getProgress(7, i, weight, userWeights, userAllWeights, column).charAt(0);
            column.put("progressWeekColor", c7 == '+' ? RED_INFO_TEXT : c7 == '-' ? GREEN_INFO_TEXT : NEUTRAL_INFO_TEXT);

            char c14 = getProgress(14, i, weight, userWeights, userAllWeights, column).charAt(0);
            column.put("progress2WeeksColor", c14 == '+' ? RED_INFO_TEXT : c14 == '-' ? GREEN_INFO_TEXT : NEUTRAL_INFO_TEXT);

            char c30 = getProgress(30, i, weight, userWeights, userAllWeights, column).charAt(0);
            column.put("progressMonthColor", c30 == '+' ? RED_INFO_TEXT : c30 == '-' ? GREEN_INFO_TEXT : NEUTRAL_INFO_TEXT);

            char cp = commonProgress.charAt(0);
            column.put("commonProgressColor", cp == '+' ? RED_INFO_TEXT : cp == '-' ? GREEN_INFO_TEXT : NEUTRAL_INFO_TEXT);
            column.put("progress3Days", getProgress(3, i, weight, userWeights, userAllWeights, column));
            column.put("progressWeek", getProgress(7, i, weight, userWeights, userAllWeights, column));
            column.put("progress2Weeks", getProgress(14, i, weight, userWeights, userAllWeights, column));
            column.put("progressMonth", getProgress(30, i, weight, userWeights, userAllWeights, column));
            column.put("info", "info_" + i);
            column.put("col", "_" + i);

            data.add(column);
        }
        return data;
    }

    public List<Map<Integer, Double>> getYdata(User user, String scale) {
        String scaleDate = getScaleDate(scale);
        List<Double> doubles = getUserWeights(user, scaleDate);
        Map<String, Double> minMax = getMinMaxForYAxis(doubles);
        List<Double> list = new ArrayList<>();
        double dif = minMax.get("max") - minMax.get("min");
        double step = dif <= 5 ? 1 : dif <= 10 ? 2 : dif <= 20 ? 4 : dif / 5;
        for (double i = minMax.get("min"); i <= minMax.get("max"); i += step) {
            list.add(i);
        }

        List<Map<Integer, Double>> res = new ArrayList<>();
        for (int i = 0; i < list.size(); i++) {
            int stepY = 320 / (list.size() - 1);
            Map<Integer, Double> item = new TreeMap<>();
            item.put(stepY * i, list.get(i));
            res.add(item);
        }
        return res;
    }

}
