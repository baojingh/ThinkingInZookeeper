package com.yxt.bd.jute;

import lombok.Getter;
import lombok.Setter;
import org.apache.jute.*;
import org.apache.zookeeper.server.ByteBufferInputStream;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;

/**
 * @Author: hebj
 * @Date: 2020/3/21 21:34
 * @Description:
 */
public class SerDeserJute {

    public static void main(String[] args) throws IOException {
        //实现Record接口，自定义序列化
        RequestHeaderDemo requestHeader = new RequestHeaderDemo(1, 22);
        System.out.print("requestHeader:  " +requestHeader );
        //序列化
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        BinaryOutputArchive binaryOutputArchive = BinaryOutputArchive.getArchive(outputStream);
        requestHeader.serialize(binaryOutputArchive,"header");
        //通常是TCP网络通信对象
        ByteBuffer bb = ByteBuffer.wrap(outputStream.toByteArray());
        //反序列化
        RequestHeaderDemo requestHeader1 = new RequestHeaderDemo();
        ByteBufferInputStream inputStream = new ByteBufferInputStream(bb);
        BinaryInputArchive binaryInputArchive =  BinaryInputArchive.getArchive(inputStream);
        requestHeader1.deserialize(binaryInputArchive,"header");
        System.out.print("requestHeader1:  " + requestHeader1);
        outputStream.close();
        inputStream.close();
    }

}

@Getter
@Setter
class RequestHeaderDemo implements Record {

    private int xid;
    private int type;

    public RequestHeaderDemo(int xid, int type) {
        this.xid = xid;
        this.type = type;
    }

    public RequestHeaderDemo() {
    }

    public void serialize(OutputArchive outputArchive, String s) throws IOException {
        outputArchive.startRecord(this, s);
        outputArchive.writeInt(xid, "xid");
        outputArchive.writeInt(type, "type");
        outputArchive.endRecord(this, s);
    }

    public void deserialize(InputArchive inputArchive, String s) throws IOException {
        inputArchive.startRecord(s);
        xid = inputArchive.readInt("xid");
        type = inputArchive.readInt("type");
        inputArchive.endRecord(s);
    }

    @Override
    public String toString() {
        return "RequestHeaderDemo{" +
                "xid=" + xid +
                ", type=" + type +
                '}';
    }
}
