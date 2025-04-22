package net.pmolinav.bookings.security;

import lombok.Getter;

import javax.servlet.ServletOutputStream;
import javax.servlet.WriteListener;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintWriter;

public class HttpResponseWrapper extends HttpServletResponseWrapper {
    private final ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
    private ServletOutputStream servletOutputStream;
    private final PrintWriter printWriter = new PrintWriter(byteArrayOutputStream);
    @Getter
    private final String correlationUid;

    public HttpResponseWrapper(HttpServletResponse response, String correlationUid) {
        super(response);
        this.correlationUid = correlationUid;
        response.setHeader("Correlation-Uid", correlationUid);
    }

    @Override
    public ServletOutputStream getOutputStream() throws IOException {
        if (servletOutputStream == null) {
            servletOutputStream = new ServletOutputStream() {
                @Override
                public boolean isReady() {
                    return true;
                }

                @Override
                public void setWriteListener(WriteListener writeListener) {
                }

                @Override
                public void write(int b) throws IOException {
                    byteArrayOutputStream.write(b);
                }
            };
        }
        return servletOutputStream;
    }

    @Override
    public PrintWriter getWriter() throws IOException {
        return printWriter;
    }

    @Override
    public void flushBuffer() throws IOException {
        printWriter.flush();
        super.flushBuffer();
    }

    public byte[] getResponseData() {
        try {
            printWriter.flush();
            return byteArrayOutputStream.toByteArray();
        } catch (Exception e) {
            return new byte[0];
        }
    }

    public void copyBodyToResponse() throws IOException {
        byte[] responseData = getResponseData();
        getResponse().getOutputStream().write(responseData);
        getResponse().flushBuffer();
    }
}
