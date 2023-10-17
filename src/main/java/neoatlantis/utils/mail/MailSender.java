package neoatlantis.utils.mail;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Properties;
import java.util.UUID;
import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
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

/**
 *
 * @author Hiryu (aslhiryu@gmail.com)
 */
public class MailSender {
    private Properties config;
    
    
    
    // Constructures -----------------------------------------------------------
    
    public MailSender(Properties config){
        this.config=config;
    }
    
    

    
    
    // Metodos publicos---------------------------------------------------------
    
    public void sendMail(String subject, String from, String to, String msg) throws MessagingException{
        sendMail(subject, from, new String[]{to}, null, null, msg, null);
    }

    public void sendMail(String subject, String from, String to, String msg, File[] attachments) throws MessagingException{
        sendMail(subject, from, new String[]{to}, null, null, msg, attachments);
    }

    public void sendMail(String subject, String from, String[] to, String msg) throws MessagingException{
        sendMail(subject, from, to, null, null, msg, null);
    }

    public void sendMail(String subject, String from, String[] to, String msg, File[] attachments) throws MessagingException{
        sendMail(subject, from, to, null, null, msg, attachments);
    }

    public void sendMail(String subject, String from, String[] to, String[] cc, String msg) throws MessagingException{
        sendMail(subject, from, to, cc, null, msg, null);
    }

    public void sendMail(String subject, String from, String[] to, String[] cc, String msg, File[] attachments) throws MessagingException{
        sendMail(subject, from, to, cc, null, msg, attachments);
    }
    
    public void sendMail(String subject, String from, String[] to, String[] cc, String[] bcc, String msg, File[] attachments) throws MessagingException{
        MimeBodyPart attachPart;
        DataSource source;
        
        Session session = Session.getDefaultInstance(this.config, null);
        MimeMessage message = new MimeMessage(session);
        message.setFrom(new InternetAddress(from));
        message.setSubject(subject);
        
        if( to==null || to.length==0 ){
            throw new MessagingException("No se proporciono destinatarios");
        }
        for(int i=0; i<to.length; i++){
            message.addRecipient(Message.RecipientType.TO, new InternetAddress(to[i]));
        }
        
        for(int i=0; cc!=null&&i<cc.length; i++){
            message.addRecipient(Message.RecipientType.CC, new InternetAddress(cc[i]));
        }

        for(int i=0; bcc!=null&&i<bcc.length; i++){
            message.addRecipient(Message.RecipientType.BCC, new InternetAddress(bcc[i]));
        }

        Multipart multipart = new MimeMultipart("alternative");
        multipart.addBodyPart(buildHtmlTextPart(msg));
        
        for(int i=0; attachments!=null&&i<attachments.length; i++){
            attachPart = new MimeBodyPart();
            source = new FileDataSource(attachments[i]);
            attachPart.setDataHandler(new DataHandler(source));
            attachPart.setFileName(attachments[i].getName());
            
            multipart.addBodyPart(attachPart);
        }
        
        message.setContent(multipart);
        
        Transport.send(message);
    }

    
    public String sendOutlookCalendar(String subject, String from, String to, String location, Date start, int duration) throws MessagingException{
        return sendOutlookCalendar(null, subject, from, new String[]{to}, null, location, "", start, duration);
    }
    
    public String sendOutlookCalendar(String uid, String subject, String from, String to, String location, Date start, int duration) throws MessagingException{
        return sendOutlookCalendar(uid, subject, from, new String[]{to}, null, location, "", start, duration);
    }
    
    public String sendOutlookCalendar(String subject, String from, String[] to, String location, Date start, int duration) throws MessagingException{
        return sendOutlookCalendar(null, subject, from, to, null, location, "", start, duration);
    }
    
    public String sendOutlookCalendar(String uid, String subject, String from, String[] to, String location, Date start, int duration) throws MessagingException{
        return sendOutlookCalendar(uid, subject, from, to, null, location, "", start, duration);
    }
    
    public String sendOutlookCalendar(String subject, String from, String[] to, String location, String description, Date start, int duration) throws MessagingException{
        return sendOutlookCalendar(null, subject, from, to, null, location, description, start, duration);
    }
    
    public String sendOutlookCalendar(String uid, String subject, String from, String[] to, String location, String description, Date start, int duration) throws MessagingException{
        return sendOutlookCalendar(uid, subject, from, to, null, location, description, start, duration);
    }
    
    public String sendOutlookCalendar(String uid, String subject, String from, String[] to, String[] cc, String location, String description, Date start, int duration) throws MessagingException{
        MimetypesFileTypeMap mimetypes = (MimetypesFileTypeMap)MimetypesFileTypeMap.getDefaultFileTypeMap();
        mimetypes.addMimeTypes("text/calendar ics ICS");
        MailcapCommandMap mailcap = (MailcapCommandMap) MailcapCommandMap.getDefaultCommandMap();
        mailcap.addMailcap("text/calendar;; x-java-content-handler=com.sun.mail.handlers.text_plain");

        //valido si es un uid valido, si no egenero uno
        if(uid==null || uid.isEmpty()){
            uid=UUID.randomUUID().toString();
        }
        
        Session session = Session.getDefaultInstance(this.config, null);
        MimeMessage message = new MimeMessage(session);
        message.setFrom(new InternetAddress(from));
        message.setSubject(subject);
        
        if( to==null || to.length==0 ){
            throw new MessagingException("No se proporciono destinatarios");
        }
        for(int i=0; i<to.length; i++){
            message.addRecipient(Message.RecipientType.TO, new InternetAddress(to[i]));
        }
        
        for(int i=0; cc!=null&&i<cc.length; i++){
            message.addRecipient(Message.RecipientType.CC, new InternetAddress(cc[i]));
        }

        Multipart multipart = new MimeMultipart("alternative");
        multipart.addBodyPart(buildHtmlTextPart(description));
        multipart.addBodyPart(buildCalendarPart(uid, to[0], location, description, start, duration));
        
        message.setContent(multipart);
        Transport.send(message);
        
        return uid;
    }


    
    
    // Metodos privados --------------------------------------------------------
    
    private BodyPart buildHtmlTextPart(String description) throws MessagingException {
        MimeBodyPart descriptionPart = new MimeBodyPart();
        descriptionPart.setContent(description, "text/html; charset=utf-8");

        return descriptionPart;
    }

    private BodyPart buildCalendarPart(String uid, String to, String location, String description, Date start, int duration) throws MessagingException {
        SimpleDateFormat iCalendarDateFormat = new SimpleDateFormat("yyyyMMdd'T'HHmm'00'");
        BodyPart calendarPart = new MimeBodyPart();

        Calendar cal = Calendar.getInstance();
        cal.setTime(start);
        cal.add(Calendar.MINUTE, duration);
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
                + "UID:"+uid+"\n"
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
