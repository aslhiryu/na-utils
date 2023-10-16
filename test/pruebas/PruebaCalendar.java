package pruebas;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Properties;
import javax.activation.DataHandler;
import javax.activation.MailcapCommandMap;
import javax.activation.MimetypesFileTypeMap;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.mail.util.ByteArrayDataSource;
import neoAtlantis.utils.mail.MailSender;

/**
 *
 * @author desarrollo.alberto
 */
public class PruebaCalendar {
    private static Properties prop = new Properties();
    private static SimpleDateFormat sdf=new SimpleDateFormat("dd/MM/yyyy HH:mm");
    
    static{
        prop.put("mail.smtp.host", "mail.economia.gob.mx");
        prop.put("mail.smtp.port", "25");
    }

    public static void main(String[] args) throws Exception {
        //(new PruebaCalendar()).send();
        //(new PruebaCalendar()).send2("desarrollo.alberto@economia.gob.mx", "desarrollo.alberto@economia.gob.mx", "Cita de prueba", "Oficiona de Alberto", "Para validar funcionalidad");
        //(new MailSender(prop)).sendOutlookCalendar(null, "Reunion con Oracle", "desarrollo.alberto@economia.gob.mx", "desarrollo.alberto@economia.gob.mx", 
                //"Sala de juntas DGTIC", "<b>Reunión</b> para probar los temas mas avanzados", sdf.parse("12/12/2014 15:00"), 60);
        //(new MailSender(prop)).sendMail("Mail de prueba", "desarrollo.alberto@economia.gob.mx",    "desarrollo.alberto@economia.gob.mx", "<h3>texto</h3>");
                //new String[]{"desarrollo.alberto@economia.gob.mx"}, 
                //new String[]{"desarrollo.alberto@economia.gob.mx"}, 
                //new String[]{"desarrollo.alberto@economia.gob.mx"}, 
                //"<b>blla</b> <i>adlansdoasindoada</i><br><img src='seWatermark.png' />", new File[]{new File("d:/seWatermark.png")});
        (new MailSender(prop)).sendOutlookCalendar("Reunion con Oracle", "desarrollo.alberto@economia.gob.mx", "desarrollo.alberto@economia.gob.mx", "Oficiona de Alberto", 
                sdf.parse("12/12/2014 15:00"), 60);
    }

    public void send2(String from, String to, String subject, String location, String description) throws Exception {
        MimetypesFileTypeMap mimetypes = (MimetypesFileTypeMap)MimetypesFileTypeMap.getDefaultFileTypeMap();
        mimetypes.addMimeTypes("text/calendar ics ICS");
        
        MailcapCommandMap mailcap = (MailcapCommandMap) MailcapCommandMap.getDefaultCommandMap();
        mailcap.addMailcap("text/calendar;; x-java-content-handler=com.sun.mail.handlers.text_plain");

        Properties prop = new Properties();
        prop.put("mail.smtp.host", "mail.economia.gob.mx");
        prop.put("mail.smtp.port", "25");
            
        Session session = Session.getDefaultInstance(prop, null);
        MimeMessage message = new MimeMessage(session);
        message.setFrom(new InternetAddress(from));
        message.setSubject(subject);
        message.addRecipient(Message.RecipientType.TO, new InternetAddress(to));

        Multipart multipart = new MimeMultipart("alternative");
        
        BodyPart messageBodyPart = buildHtmlTextPart(description);
        multipart.addBodyPart(messageBodyPart);
        
        BodyPart calendarPart = buildCalendarPart(to, location, description);
        multipart.addBodyPart(calendarPart);
        
        message.setContent(multipart);

        // send the message
        /*Transport transport = session.getTransport("smtp");
        transport.connect();
        transport.sendMessage(message, message.getAllRecipients());
        transport.close();*/
        Transport.send(message);
    }
    
    public boolean send() throws Exception {
        Date reviewDateStartTime = new Date();
        String fromMail;
        String toMail;
        String uniqueId = "123456";
        String reviewDescription = "testing only";
        String reviewSubject = "testing review subject";
        String title = "testing";
        String summary = "testing summary";
        toMail = "desarrollo.alberto@economia.gob.mx";
        int reviewDurationMnts = 30;
        String reviewLocation = "test location";
        fromMail = toMail;
        String meetingStartTime = getReviewTime(reviewDateStartTime,
                reviewDurationMnts, false);
        String meetingEndTime = getReviewTime(reviewDateStartTime,
                reviewDurationMnts, true);

        Properties prop = new Properties();
        StringBuffer sb = new StringBuffer();
        StringBuffer buffer = null;
        Session session = Session.getDefaultInstance(prop, null);
        Message message = new MimeMessage(session);


        try {
            prop.put("mail.smtp.host", "mail.economia.gob.mx");
            prop.put("mail.smtp.port", "25");

// Define message
            message.setFrom(new InternetAddress(fromMail));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(toMail, false));
            message.setSubject(reviewSubject);
            message.setHeader("X-Mailer", "test-Mailer");
// Create the message part
            MimeBodyPart messageBodyPart = new MimeBodyPart();
            buffer = sb
                    .append("BEGIN:VCALENDAR\n"
                    + "PRODID:-//Microsoft Corporation//Outlook 9.0 MIMEDIR//EN\n"
                    + "VERSION:2.0\n" + "METHOD:REQUEST\n"
                    + "BEGIN:VEVENT\n"
                    + "ATTENDEE;ROLE=REQ-PARTICIPANT;RSVP=TRUE:MAILTO:"
                    + toMail + "\n" + "ORGANIZER:MAILTO:" + fromMail + "\n"
                    + "DTSTART:" + meetingStartTime + "\n" + "DTEND:"
                    + meetingEndTime + "\n" + "LOCATION:"
                    + reviewLocation + "\n" + "TRANSP:OPAQUE\n"
                    + "SEQUENCE:0\n" + "UID:" + uniqueId
                    + "@iquest.com\n" + "DTSTAMP:" + meetingEndTime
                    + "\n" + "CATEGORIES:Meeting\n" + "DESCRIPTION:"
                    + reviewDescription + ".\n\n" + "SUMMARY:"
                    + summary + "\n" + "PRIORITY:1\n"
                    + "CLASS:PUBLIC\n" + "BEGIN:VALARM\n"
                    + "TRIGGER:PT1440M\n" + "ACTIONISPLAY\n"
                    + "DESCRIPTION:Reminder\n" + "END:VALARM\n"
                    + "END:VEVENT\n" + "END:VCALENDAR");
            messageBodyPart.setFileName("TestMeeting.ics");
            messageBodyPart
                    .setDataHandler(new DataHandler(new ByteArrayDataSource(buffer.toString(), "text/iCalendar")));
            messageBodyPart.setHeader("Content-Class",
                    "urn:content-classes:calendarmessage");
            messageBodyPart.setHeader("Content-ID", "calendar_message");
            Multipart multipart = new MimeMultipart();
            multipart.addBodyPart(messageBodyPart);
            message.setContent(multipart);
            Transport.send(message);

        } catch (Exception me) {
            me.printStackTrace();
        }
        return false;
    }
    
    public String getReviewTime(Date reviewDateTime, int rDuration, boolean flag) {
        Calendar c = Calendar.getInstance();
        SimpleDateFormat s = new SimpleDateFormat("yyyyMMdd");
        c.setTime(reviewDateTime);
        if (flag == true) {
            c.add(Calendar.MINUTE, rDuration);
        }
        String hour = c.get(Calendar.HOUR_OF_DAY) < 10 ? "0"
                + c.get(Calendar.HOUR_OF_DAY) : ""
                + c.get(Calendar.HOUR_OF_DAY);
        String min = c.get(Calendar.MINUTE) < 10 ? "0" + c.get(Calendar.MINUTE)
                : "" + c.get(Calendar.MINUTE);
        String sec = c.get(Calendar.SECOND) < 10 ? "0" + c.get(Calendar.SECOND)
                : "" + c.get(Calendar.SECOND);

        String date = s.format(new Date(c.getTimeInMillis()));
        String dateTime = date + "T" + hour + min + sec;
        return dateTime;
    }

    
    private BodyPart buildHtmlTextPart(String description) throws MessagingException {
        MimeBodyPart descriptionPart = new MimeBodyPart();
        descriptionPart.setContent(description, "text/html; charset=utf-8");

        return descriptionPart;
    }

    private BodyPart buildCalendarPart(String to, String location, String description) throws Exception {
        SimpleDateFormat iCalendarDateFormat = new SimpleDateFormat("yyyyMMdd'T'HHmm'00'");
        BodyPart calendarPart = new MimeBodyPart();

        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DAY_OF_MONTH, 1);
        Date start = cal.getTime();
        cal.add(Calendar.HOUR_OF_DAY, 3);
        Date end = cal.getTime();

        //check the icalendar spec in order to build a more complicated meeting request
        String calendarContent =
                "BEGIN:VCALENDAR\n"
                + "METHOD:REQUEST\n"
                + "PRODID: BCP - Meeting\n"
                + "VERSION:2.0\n"
                + "BEGIN:VEVENT\n"
                + "DTSTAMP:" + iCalendarDateFormat.format(start) + "\n"
                + "DTSTART:" + iCalendarDateFormat.format(start) + "\n"
                + "DTEND:" + iCalendarDateFormat.format(end) + "\n"
                + "SUMMARY:"+description+"\n"
                + "UID:324\n"
                + "ATTENDEE;ROLE=REQ-PARTICIPANT;PARTSTAT=NEEDS-ACTION;RSVP=TRUE:MAILTO:"+to+"\n"
                + "ORGANIZER:MAILTO:"+to+"\n"
                + "LOCATION:"+location+"\n"
                + "DESCRIPTION:"+description+"\n"
                + "SEQUENCE:0\n"
                + "PRIORITY:5\n"
                + "CLASS:PUBLIC\n"
                + "STATUS:CONFIRMED\n"
                + "TRANSP:OPAQUE\n"
                + "BEGIN:VALARM\n"
                + "ACTION:DISPLAY\n"
                + "DESCRIPTION:REMINDER\n"
                + "TRIGGER;RELATED=START:-PT00H15M00S\n"
                + "END:VALARM\n"
                + "END:VEVENT\n"
                + "END:VCALENDAR";

        calendarPart.addHeader("Content-Class", "urn:content-classes:calendarmessage");
        calendarPart.setContent(calendarContent, "text/calendar;method=CANCEL");

        return calendarPart;
    }

}
