package com.engineering.rabbitmq.model;

public class Chunk {
    private String fileId;
    private int chunkIndex;
    private int totalChunks;
    private byte[] data;

    public Chunk(String fileId, int chunkIndex, int totalChunks, byte[] data){
        this.fileId=fileId;
        this.chunkIndex=chunkIndex;
        this.totalChunks=totalChunks;
        this.data=data;
    }

    public String getFileId() {
        return fileId;
    }

    public void setFileId(String fileId) {
        this.fileId = fileId;
    }

    public int getChunkIndex() {
        return chunkIndex;
    }

    public void setChunkIndex(int chunkIndex) {
        this.chunkIndex = chunkIndex;
    }

    public int getTotalChunks() {
        return totalChunks;
    }

    public void setTotalChunks(int totalChunks) {
        this.totalChunks = totalChunks;
    }

    public byte[] getData() {
        return data;
    }

    public void setData(byte[] data) {
        this.data = data;
    }
}
