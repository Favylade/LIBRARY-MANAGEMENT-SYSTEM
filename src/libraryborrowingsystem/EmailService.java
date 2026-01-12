package libraryborrowingsystem;

import jakarta.mail.*;
import jakarta.mail.internet.*;
import java.util.Properties;
import java.time.LocalDate;

public class EmailService {

    public static void sendUserIdEmail(String recipientEmail, String userId) {

        final String senderEmail = "favourololade02@gmail.com";
        final String senderPassword = "nfjo jjnm qgoa rzmr"; // Gmail App Password

        Properties props = new Properties();

        // ✅ SSL configuration
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "465");
        props.put("mail.smtp.ssl.enable", "true");
        props.put("mail.smtp.auth", "true");

        // ⏱ timeouts
        props.put("mail.smtp.connectiontimeout", "5000");
        props.put("mail.smtp.timeout", "5000");
        props.put("mail.smtp.writetimeout", "5000");

        Session session = Session.getInstance(props, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(senderEmail, senderPassword);
            }
        });

        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(senderEmail));
            message.setRecipients(
                    Message.RecipientType.TO,
                    InternetAddress.parse(recipientEmail)
            );
            message.setSubject("Your Library User ID");

            message.setText(
                    "Welcome to the Library System!\n\n"
                    + "Your unique User ID is: " + userId + "\n\n"
                    + "Please keep it safe.\n\n"
                    + "Library Management"
            );

            Transport.send(message);
            System.out.println("User ID email sent successfully.");

        } catch (MessagingException e) {
            System.err.println("Failed to send email: " + e.getMessage());
        }
    }

    public static void sendBorrowConfirmationEmail(
            String recipientEmail,
            String bookTitle,
            LocalDate borrowDate,
            LocalDate dueDate
    ) {
        final String senderEmail = "favourololade02@gmail.com";
        final String senderPassword = "nfjo jjnm qgoa rzmr"; // keep same one

        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.ssl.enable", "true");
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "465");

        Session session = Session.getInstance(props, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(senderEmail, senderPassword);
            }
        });

        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(senderEmail));
            message.setRecipients(
                    Message.RecipientType.TO,
                    InternetAddress.parse(recipientEmail)
            );
            message.setSubject("📚 Book Borrowed Successfully");

            message.setText(
                    "Dear Library User,\n\n"
                    + "You have successfully borrowed a book.\n\n"
                    + "📖 Book Title: " + bookTitle + "\n"
                    + "📅 Borrow Date: " + borrowDate + "\n"
                    + "⏳ Due Date: " + dueDate + "\n\n"
                    + "Please return the book on or before the due date to avoid fines.\n\n"
                    + "Library Management System"
            );

            Transport.send(message);

        } catch (MessagingException e) {
            System.err.println("Email sending failed: " + e.getMessage());
        }
    }

    public static void sendReturnConfirmationEmail(
            String recipientEmail,
            String bookTitle,
            LocalDate returnDate
    ) {
        final String senderEmail = "favourololade02@gmail.com";
        final String senderPassword = "nfjo jjnm qgoa rzmr"; // same app password

        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.ssl.enable", "true");
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "465");

        Session session = Session.getInstance(props, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(senderEmail, senderPassword);
            }
        });

        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(senderEmail));
            message.setRecipients(
                    Message.RecipientType.TO,
                    InternetAddress.parse(recipientEmail)
            );
            message.setSubject("📖 Book Returned Successfully");

            message.setText(
                    "Dear Library User,\n\n"
                    + "Thank you for returning your borrowed book.\n\n"
                    + "📚 Book Title: " + bookTitle + "\n"
                    + "📅 Return Date: " + returnDate + "\n\n"
                    + "We appreciate your responsibility.\n\n"
                    + "Library Management System"
            );

            Transport.send(message);

        } catch (MessagingException e) {
            System.err.println("Return email failed: " + e.getMessage());
        }
    }

}
