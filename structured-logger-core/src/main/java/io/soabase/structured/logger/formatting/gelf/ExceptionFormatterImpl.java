/**
 * Copyright 2019 Jordan Zimmerman
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.soabase.structured.logger.formatting.gelf;

import io.soabase.structured.logger.formatting.DefaultLoggingFormatter;

import java.io.IOException;
import java.io.InterruptedIOException;
import java.io.LineNumberReader;
import java.io.PrintWriter;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.ArrayList;

public class ExceptionFormatterImpl implements ExceptionFormatter {
    private final int maxExceptionLines;

    private static final int DEFAULT_MAX_EXCEPTION_LINES = 100;

    public ExceptionFormatterImpl() {
        this(DEFAULT_MAX_EXCEPTION_LINES);
    }

    public ExceptionFormatterImpl(int maxExceptionLines) {
        this.maxExceptionLines = maxExceptionLines;
    }

    @Override
    public String format(Throwable e) {
        return formatLinesToString(formatExceptionToLines(e));
    }

    // ---- below mostly copied from org.apache.log4j.DefaultThrowableRenderer.java

    private String formatLinesToString(String[] stringRep) {
        StringBuilder str = new StringBuilder();
        int length = stringRep.length;
        if (maxExceptionLines < 0) {
            length += maxExceptionLines;
        } else if (length > maxExceptionLines) {
            length = maxExceptionLines;
        }

        for (int i = 0; i < length; i++) {
            String string = stringRep[i];
            addEscapedValue(str, string);
            str.append("\n");
        }
        return str.toString();
    }

    @SuppressWarnings("unchecked")
    private String[] formatExceptionToLines(Throwable throwable) {
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        try {
            throwable.printStackTrace(pw);
        } catch(RuntimeException ignore) {
            // ignore
        }
        pw.flush();
        LineNumberReader reader = new LineNumberReader(
                new StringReader(sw.toString()));
        ArrayList lines = new ArrayList();
        try {
            String line = reader.readLine();
            while(line != null) {
                lines.add(line);
                line = reader.readLine();
            }
        } catch(IOException ex) {
            if (ex instanceof InterruptedIOException) {
                Thread.currentThread().interrupt();
            }
            lines.add(ex.toString());
        }
        String[] tempRep = new String[lines.size()];
        //noinspection SuspiciousToArrayCall
        lines.toArray(tempRep);
        return tempRep;
    }

    private void addEscapedValue(StringBuilder builder, String str) {
        for (int i = 0; i < str.length(); ++i) {
            char c = str.charAt(i);
            switch (c) {
                case '"': {
                    builder.append('\\').append(c);
                    break;
                }

                case '\r': {
                    builder.append("\\r");
                    break;
                }

                case '\n': {
                    builder.append("\\n");
                    break;
                }

                default: {
                    if (Character.isISOControl(c)) {
                        builder.append(' ');
                    } else {
                        builder.append(c);
                    }
                    break;
                }
            }
        }
    }
}
