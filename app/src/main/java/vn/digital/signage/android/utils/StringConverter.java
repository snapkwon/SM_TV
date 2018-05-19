package vn.digital.signage.android.utils;

import android.text.TextUtils;
import android.util.Log;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.util.Vector;

import retrofit.converter.ConversionException;
import retrofit.converter.Converter;
import retrofit.mime.TypedInput;
import retrofit.mime.TypedOutput;

/**
 * The type String converter.
 */
public class StringConverter implements Converter {

    private String mCharset = ""; // "ISO-8859-1"

    /**
     * Instantiates a new String converter.
     */
    public StringConverter() {
    }

    /**
     * Instantiates a new String converter.
     *
     * @param charset the charset
     */
    public StringConverter(String charset) {
        mCharset = charset;
    }

    /**
     * From stream string.
     *
     * @param in the in
     * @return the string
     * @throws IOException the io exception
     */
    public static String fromStream(InputStream in) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(in));
        StringBuilder out = new StringBuilder();
        String newLine = System.getProperty("line.separator");
        String line;
        while ((line = reader.readLine()) != null) {
            out.append(line);
            out.append(newLine);
        }
        return out.toString();
    }

    /**
     * From stream string.
     *
     * @param in      the in
     * @param charset the charset
     * @return the string
     * @throws IOException the io exception
     */
    public static String fromStream(InputStream in, String charset) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(in, charset));
        StringBuilder out = new StringBuilder();
        String newLine = System.getProperty("line.separator");
        String line;
        while ((line = reader.readLine()) != null) {
            out.append(line);
            out.append(newLine);
        }
        return out.toString();
    }

    /**
     * From utf 8 byte array string.
     *
     * @param bytes the bytes
     * @return the string
     */
    public static String fromUTF8ByteArray(byte[] bytes) {
        int i = 0;
        int length = 0;

        while (i < bytes.length) {
            length++;
            if ((bytes[i] & 0xf0) == 0xf0) {
                // surrogate pair
                length++;
                i += 4;
            } else if ((bytes[i] & 0xe0) == 0xe0) {
                i += 3;
            } else if ((bytes[i] & 0xc0) == 0xc0) {
                i += 2;
            } else {
                i += 1;
            }
        }

        char[] cs = new char[length];

        i = 0;
        length = 0;

        while (i < bytes.length) {
            char ch;

            if ((bytes[i] & 0xf0) == 0xf0) {
                int codePoint = ((bytes[i] & 0x03) << 18) | ((bytes[i + 1] & 0x3F) << 12) | ((bytes[i + 2] & 0x3F) << 6) | (bytes[i + 3] & 0x3F);
                int U = codePoint - 0x10000;
                char W1 = (char) (0xD800 | (U >> 10));
                char W2 = (char) (0xDC00 | (U & 0x3FF));
                cs[length++] = W1;
                ch = W2;
                i += 4;
            } else if ((bytes[i] & 0xe0) == 0xe0) {
                ch = (char) (((bytes[i] & 0x0f) << 12)
                        | ((bytes[i + 1] & 0x3f) << 6) | (bytes[i + 2] & 0x3f));
                i += 3;
            } else if ((bytes[i] & 0xd0) == 0xd0) {
                ch = (char) (((bytes[i] & 0x1f) << 6) | (bytes[i + 1] & 0x3f));
                i += 2;
            } else if ((bytes[i] & 0xc0) == 0xc0) {
                ch = (char) (((bytes[i] & 0x1f) << 6) | (bytes[i + 1] & 0x3f));
                i += 2;
            } else {
                ch = (char) (bytes[i] & 0xff);
                i += 1;
            }

            cs[length++] = ch;
        }

        return new String(cs);
    }

    /**
     * To utf 8 byte array byte [ ].
     *
     * @param string the string
     * @return the byte [ ]
     */
    public static byte[] toUTF8ByteArray(String string) {
        return toUTF8ByteArray(string.toCharArray());
    }

    /**
     * To utf 8 byte array byte [ ].
     *
     * @param string the string
     * @return the byte [ ]
     */
    public static byte[] toUTF8ByteArray(char[] string) {
        ByteArrayOutputStream bOut = new ByteArrayOutputStream();
        char[] c = string;
        int i = 0;

        while (i < c.length) {
            char ch = c[i];

            if (ch < 0x0080) {
                bOut.write(ch);
            } else if (ch < 0x0800) {
                bOut.write(0xc0 | (ch >> 6));
                bOut.write(0x80 | (ch & 0x3f));
            }
            // surrogate pair
            else if (ch >= 0xD800 && ch <= 0xDFFF) {
                // in error - can only happen, if the Java String class has a
                // bug.
                if (i + 1 >= c.length) {
                    throw new IllegalStateException("invalid UTF-16 codepoint");
                }
                char W1 = ch;
                ch = c[++i];
                char W2 = ch;
                // in error - can only happen, if the Java String class has a
                // bug.
                if (W1 > 0xDBFF) {
                    throw new IllegalStateException("invalid UTF-16 codepoint");
                }
                int codePoint = (((W1 & 0x03FF) << 10) | (W2 & 0x03FF)) + 0x10000;
                bOut.write(0xf0 | (codePoint >> 18));
                bOut.write(0x80 | ((codePoint >> 12) & 0x3F));
                bOut.write(0x80 | ((codePoint >> 6) & 0x3F));
                bOut.write(0x80 | (codePoint & 0x3F));
            } else {
                bOut.write(0xe0 | (ch >> 12));
                bOut.write(0x80 | ((ch >> 6) & 0x3F));
                bOut.write(0x80 | (ch & 0x3F));
            }

            i++;
        }

        return bOut.toByteArray();
    }

    /**
     * To upper case string.
     *
     * @param string the string
     * @return the string
     */
    public static String toUpperCase(String string) {
        boolean changed = false;
        char[] chars = string.toCharArray();

        for (int i = 0; i != chars.length; i++) {
            char ch = chars[i];
            if ('a' <= ch && 'z' >= ch) {
                changed = true;
                chars[i] = (char) (ch - 'a' + 'A');
            }
        }

        if (changed) {
            return new String(chars);
        }

        return string;
    }

    /**
     * To lower case string.
     *
     * @param string the string
     * @return the string
     */
    public static String toLowerCase(String string) {
        boolean changed = false;
        char[] chars = string.toCharArray();

        for (int i = 0; i != chars.length; i++) {
            char ch = chars[i];
            if ('A' <= ch && 'Z' >= ch) {
                changed = true;
                chars[i] = (char) (ch - 'A' + 'a');
            }
        }

        if (changed) {
            return new String(chars);
        }

        return string;
    }

    /**
     * To byte array byte [ ].
     *
     * @param chars the chars
     * @return the byte [ ]
     */
    public static byte[] toByteArray(char[] chars) {
        byte[] bytes = new byte[chars.length];

        for (int i = 0; i != bytes.length; i++) {
            bytes[i] = (byte) chars[i];
        }

        return bytes;
    }

    /**
     * To byte array byte [ ].
     *
     * @param string the string
     * @return the byte [ ]
     */
    public static byte[] toByteArray(String string) {
        byte[] bytes = new byte[string.length()];

        for (int i = 0; i != bytes.length; i++) {
            char ch = string.charAt(i);

            bytes[i] = (byte) ch;
        }

        return bytes;
    }

    /**
     * Split string [ ].
     *
     * @param input     the input
     * @param delimiter the delimiter
     * @return the string [ ]
     */
    public static String[] split(String input, char delimiter) {
        Vector v = new Vector();
        boolean moreTokens = true;
        String subString;

        while (moreTokens) {
            int tokenLocation = input.indexOf(delimiter);
            if (tokenLocation > 0) {
                subString = input.substring(0, tokenLocation);
                v.addElement(subString);
                input = input.substring(tokenLocation + 1);
            } else {
                moreTokens = false;
                v.addElement(input);
            }
        }

        String[] res = new String[v.size()];

        for (int i = 0; i != res.length; i++) {
            res[i] = (String) v.elementAt(i);
        }
        return res;
    }

    @Override
    public Object fromBody(TypedInput typedInput, Type type) throws ConversionException {
        String response = null;
        try {
            if (!TextUtils.isEmpty(mCharset))
                response = fromStream(typedInput.in(), mCharset);
            else
                response = fromStream(typedInput.in());

            // simplify response
            response = Utils.Formatter.simplify(response);
        } catch (IOException ignored) {
            Log.e("RssConverter", "fromBody", ignored);
        }

        return response;
    }

    @Override
    public TypedOutput toBody(Object o) {
        return null;
    }
}