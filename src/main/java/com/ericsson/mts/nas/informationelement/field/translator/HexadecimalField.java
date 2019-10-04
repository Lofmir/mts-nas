package com.ericsson.mts.nas.informationelement.field.translator;

import com.ericsson.mts.nas.BitInputStream;
import com.ericsson.mts.nas.exceptions.DecodingException;
import com.ericsson.mts.nas.informationelement.field.AbstractTranslatorField;
import com.ericsson.mts.nas.reader.XMLFormatReader;
import com.ericsson.mts.nas.registry.Registry;
import com.ericsson.mts.nas.writer.FormatWriter;

import java.io.IOException;
import java.math.BigInteger;
import java.nio.ByteBuffer;

import static com.ericsson.mts.nas.writer.XMLFormatWriter.bytesToHex;

public class HexadecimalField extends AbstractTranslatorField {

    @Override
    public int decode(Registry mainRegistry,  BitInputStream s, FormatWriter formatWriter) throws IOException, DecodingException {
        logger.trace("Enter field {} with length {}", name, length);
        byte[] buffer;
        int len;
        if(null == length){
            len = s.bigReadBits(8).intValueExact();
        } else {
            len = this.length;
        }
        formatWriter.intValue("Length", BigInteger.valueOf(len));

        logger.trace("reading {} bits", len);
        buffer = new byte[len / 8 + ((len % 8) > 0 ? 1 : 0)];
        int offset = 7;
        int index = 0;
        while (len > 0) {
            byte bitValue = (byte) s.readBit();
            buffer[index] = (byte) (buffer[index] | (bitValue << offset));
            offset--;
            if (-1 == offset) {
                index++;
                offset = 7;
            }
            len--;
        }

        formatWriter.bytesValue(name, buffer);
        logger.trace("return buffer 0x{}", bytesToHex(buffer));
        return 0;
    }

    @Override
    public String encode(Registry mainRegistry, XMLFormatReader r, StringBuilder binaryString) {
        return "";
    }
}