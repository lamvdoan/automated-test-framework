package com.fetchrewards.common.utility;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fetchrewards.common.framework.AutomationBase;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.text.WordUtils;
import org.joda.time.DateTime;
import org.joda.time.format.ISODateTimeFormat;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.nio.ByteBuffer;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class Utils extends AutomationBase {
    public static final ObjectMapper mapper = new ObjectMapper();

    static {
        mapper.enable(SerializationFeature.INDENT_OUTPUT);
    }

    public static void sleep(int seconds) {
        log.debug("Sleeping for " + seconds + " seconds...");
        sleep(1.0 * seconds);
    }

    public static void sleep(double secs) {
        long millis = (long) (secs * 1000);

        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static ArrayList pullLinks(String text, boolean updateDomain) {
        // extract the links from a string containing html
        ArrayList links = new ArrayList();

        String regex = "\\(?\\b(http://|www[.])[-A-Za-z0-9+&@#/%?=~_()|!:,.;]*[-A-Za-z0-9+&@#/%=~_()|]";
        Pattern p = Pattern.compile(regex);
        Matcher m = p.matcher(text);
        while (m.find()) {
            String urlStr = m.group();
            if (urlStr.startsWith("(") && urlStr.endsWith(")")) {
                urlStr = urlStr.substring(1, urlStr.length() - 1);
            }
            links.add(urlStr);
        }
        return links;
    }

    public static String getCurrentTimestampString() {
        Date currentDate = new Date();
        Timestamp currentTimestamp = new Timestamp(currentDate.getTime());
        return currentTimestamp.toString();
    }

    public static String getCurrentISOTimestampString() {
        return ISODateTimeFormat.dateTime().print(DateTime.now());
        //2015-01-08 17:21:03.277859-06:00
    }

    public static String getRandomNumberBasedonTimestamp() {
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("MMddyyHHmmssSS");
        return sdf.format(date);
    }

    public static String getRandomNumberBasedonTimestamp(String format) {
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat(format);

        return sdf.format(date);
    }

    public static int getRandomInt(int min, int max) {

        // NOTE: Usually this should be a field rather than a method
        // variable so that it is not re-seeded every call.
        Random rand = new Random();

        // nextInt is normally exclusive of the top value,
        // so add 1 to make it inclusive
        return rand.nextInt((max - min) + 1) + min;
    }

    public static Integer getRandomUniqueInt(int oldValue, int min, int max) {
        Random rand = new Random();
        int proposedRandomNumber = rand.nextInt((max - min) + 1) + min;
        int uniqueNumber = Utils.getRandomInt(1,100);

        while (proposedRandomNumber == oldValue) {
            uniqueNumber = Utils.getRandomInt(1,100);
        }

        return uniqueNumber;
    }

    public static int getRandomInt(int min, int max, List<Integer> listToExclude) {
        Integer rand = getRandomInt(min, max);

        while (listToExclude.contains(rand)) {
            rand = getRandomInt(min, max);
        }

        return rand;
    }

    public static boolean getRandomBoolean() {
        Random rnd = new Random();
        return rnd.nextBoolean();
    }

    public static String getYesterdayDateString(String format) {
        return Calendar.getInstance().get(Calendar.YEAR) + "-" + getDateString("MM-dd", -1);
    }

    public static String getCurrentDateString(String format) {
        // ** Returns the current system date in specified format
        Date currentDate = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat(format);
        return formatter.format(currentDate);
    }

    public static String getCurrentDateString() {
        // ** Returns the current system date in MM/dd/yyy format
        return getCurrentDateString("MM/dd/yyyy");
    }

    public static String getFullCurrentDateTime() {
        // ** Returns the current system date in MMddyyHHmmssSS format
        return getCurrentDateString("MMddyyHHmmssSS").replace("/", "");
    }

    public static String getTomorrowDateString() {
        // ** Returns tomorrows date in MM/dd/yyy format
        return getTomorrowDateString("MM/dd/yyyy");
    }

    public static String getTomorrowDateString(String format) {
        return getDateString(format, 1);
    }

    public static String getDateString(int offset) {
        return getDateString("MM/dd/yyyy", offset);
    }

    public static Date getDate(int offset) {
        Date currentDate = new Date();
        Calendar cal = Calendar.getInstance();

        cal.setTime(currentDate);
        cal.add(Calendar.DATE, offset);

        return cal.getTime();
    }

    public static String getDateString(String format, int offset) {
        return formatDate(getDate(offset), format);
    }

    public static String formatDate(Date date, String format) {
        SimpleDateFormat formatter = new SimpleDateFormat(format);

        return formatter.format(date);
    }

    public static String formatDateToISODate(Date date) {
        return ISODateTimeFormat.dateTime().print(date.getTime());
    }

    public static String formatDate(Long dateVal, String formatString) {
        Date date = new Date(dateVal);
        return new SimpleDateFormat(formatString, Locale.ENGLISH).format(date);
    }

    public static double round(double value, int places) {
        if (places < 0) throw new IllegalArgumentException();
        return new BigDecimal(value).setScale(places, RoundingMode.HALF_UP).doubleValue();
    }

    public static String formatPhoneNumber(String phoneNumber) {
        // 2345678901
        String formattedPhone = "(";

        formattedPhone += phoneNumber.substring(0, 3);
        formattedPhone += ") ";
        formattedPhone += phoneNumber.substring(3, 6);
        formattedPhone += "-";
        formattedPhone += phoneNumber.substring(6);

        return formattedPhone;
    }

    public static String formatPhoneNumberDash(String phoneNumber) {
        // 2345678901
        String formattedPhone = "";

        formattedPhone += phoneNumber.substring(0, 3);
        formattedPhone += "-";
        formattedPhone += phoneNumber.substring(3, 6);
        formattedPhone += "-";
        formattedPhone += phoneNumber.substring(6);

        return formattedPhone;
    }

    public static String formatPhoneNumberAs10Digits(String phoneNumber) {
        String formattedNumber = phoneNumber.replaceAll("[^\\d]", "");

        if (!StringUtils.isNumeric(formattedNumber)) {
            log.error("Non-valid phone number passed in: " + phoneNumber);
        }

        return formattedNumber;
    }

    public static String capitalizeFirstLetters(String text) {
        return WordUtils.capitalize(text);
    }

    public static String GetLastDateOfLastMonth() {
        Date today = new Date();

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(today);

        calendar.add(Calendar.MONTH, 0);
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        calendar.add(Calendar.DATE, -1);

        Date lastDayOfMonth = calendar.getTime();

        DateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        log.info("Today            : " + sdf.format(today));
        log.info("Last Day of Month: " + sdf.format(lastDayOfMonth));
        return sdf.format(lastDayOfMonth);
    }

    public static UUID generateRandomUUID() {
        UUID uuid = UUID.randomUUID();
        log.trace("Randomly generated UUID : " + uuid);

        return uuid;
    }

    public static String generateEncodedRandomUUID() {
        String encodedUDID = encode(generateRandomUUID());
        log.debug("Encoded UDID:" + encodedUDID);
        return encodedUDID;
    }

    public static String encodeUuid(String uuid) {
        return encode(UUID.fromString(uuid)).toString();
    }

    public static String encode(UUID uuid) {
        ByteBuffer buffer = ByteBuffer.wrap(new byte[16]);
        buffer.putLong(uuid.getMostSignificantBits());
        buffer.putLong(uuid.getLeastSignificantBits());
        return  Base64.encodeBase64URLSafeString(buffer.array());
    }

    public static UUID decode(String input) {

        if (input.length() == 36) {
            return UUID.fromString(input);
        }

        if (input.length() != 22) {
            throw new IllegalArgumentException("Not a valid Base64 encoded UUID");
        }
        ByteBuffer buffer = ByteBuffer.wrap(Base64.decodeBase64(input));
        if (buffer.capacity() != 16) {
            throw new IllegalArgumentException("Not a valid Base64 encoded UUID");
        }
        return new UUID(buffer.getLong(), buffer.getLong());
    }

    public static String generateRandomText(String prefix) {
        return prefix + getCurrentDateString("MMddyyHHmmssSS").replace("/", "");
    }

    public static String generateRandomAlphaNumericCharacters(Integer length) {
        final String validCharacters = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        Random random = new Random(System.currentTimeMillis());
        StringBuilder sb = new StringBuilder(length);

        for (int j = 0; j < length; j++) {
            sb.append(validCharacters.charAt(random.nextInt(validCharacters.length())));
        }
        return sb.toString();
    }
}
