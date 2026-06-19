package com.actividad3.comms_service.service;

import com.actividad3.comms_service.config.MailProperties;
import org.springframework.stereotype.Component;

import javax.net.ssl.SSLSocketFactory;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

@Component
public class SmtpEmailClient {

    private final MailProperties mailProperties;

    public SmtpEmailClient(MailProperties mailProperties) {
        this.mailProperties = mailProperties;
    }

    public void send(String to, String subject, String body) {
        try (SmtpConnection connection = SmtpConnection.open(mailProperties.getHost(), mailProperties.getPort())) {
            connection.expectReady();
            connection.command("EHLO relatos.local", 250);

            if (mailProperties.isStarttls()) {
                connection.command("STARTTLS", 220);
                connection.upgradeToTls(mailProperties.getHost());
                connection.command("EHLO relatos.local", 250);
            }

            if (mailProperties.isSmtpAuth()) {
                connection.command("AUTH LOGIN", 334);
                connection.command(base64(mailProperties.getUsername()), 334);
                connection.command(base64(mailProperties.getPassword()), 235);
            }

            connection.command("MAIL FROM:<" + mailProperties.getFrom() + ">", 250);
            connection.command("RCPT TO:<" + to + ">", 250);
            connection.command("DATA", 354);
            connection.writeData(buildMessage(mailProperties.getFrom(), to, subject, body));
            connection.command("QUIT", 221);
        } catch (IOException ex) {
            throw new IllegalStateException("No se pudo enviar correo SMTP", ex);
        }
    }

    private static String buildMessage(String from, String to, String subject, String body) {
        return "From: Relatos de Papel <" + from + ">\r\n"
                + "To: " + to + "\r\n"
                + "Subject: " + subject + "\r\n"
                + "Content-Type: text/plain; charset=UTF-8\r\n"
                + "\r\n"
                + body
                + "\r\n.";
    }

    private static String base64(String value) {
        return Base64.getEncoder().encodeToString(value.getBytes(StandardCharsets.UTF_8));
    }

    private static final class SmtpConnection implements AutoCloseable {

        private Socket socket;
        private BufferedReader reader;
        private BufferedWriter writer;

        private SmtpConnection(Socket socket) throws IOException {
            this.socket = socket;
            this.reader = reader(socket);
            this.writer = writer(socket);
        }

        static SmtpConnection open(String host, int port) throws IOException {
            return new SmtpConnection(new Socket(host, port));
        }

        void expectReady() throws IOException {
            expect(220, readResponse());
        }

        void command(String command, int expectedCode) throws IOException {
            writer.write(command);
            writer.write("\r\n");
            writer.flush();
            expect(expectedCode, readResponse());
        }

        void writeData(String data) throws IOException {
            writer.write(data);
            writer.write("\r\n");
            writer.flush();
            expect(250, readResponse());
        }

        void upgradeToTls(String host) throws IOException {
            SSLSocketFactory sslSocketFactory = (SSLSocketFactory) SSLSocketFactory.getDefault();
            socket = sslSocketFactory.createSocket(socket, host, socket.getPort(), true);
            reader = reader(socket);
            writer = writer(socket);
        }

        private String readResponse() throws IOException {
            StringBuilder response = new StringBuilder();
            String line;
            do {
                line = reader.readLine();
                if (line == null) {
                    throw new IOException("Respuesta SMTP vacia");
                }
                response.append(line).append('\n');
            } while (line.length() > 3 && line.charAt(3) == '-');
            return response.toString();
        }

        private void expect(int expectedCode, String response) throws IOException {
            if (!response.startsWith(String.valueOf(expectedCode))) {
                throw new IOException("Respuesta SMTP inesperada. Esperada " + expectedCode + ", recibida: " + response);
            }
        }

        @Override
        public void close() throws IOException {
            socket.close();
        }

        private static BufferedReader reader(Socket socket) throws IOException {
            return new BufferedReader(new InputStreamReader(socket.getInputStream(), StandardCharsets.UTF_8));
        }

        private static BufferedWriter writer(Socket socket) throws IOException {
            return new BufferedWriter(new OutputStreamWriter(socket.getOutputStream(), StandardCharsets.UTF_8));
        }
    }
}
