package ua.setko.util;

import java.util.Date;

/**
 * @Author Artem Setko on 16.12.15.
 *
 * Wrapper of the one connection statistics
 */
public class ConnectionNodeUnit {
    private final String IP;
    private final String URI;
    private final Date timeStamp;
    private int sentBytes;




    public void setSentBytes(int sentBytes) {
        this.sentBytes = sentBytes;
    }
    private final int receivedBytes;
    private final long speed;

    public ConnectionNodeUnit(String IP, String URI, int sentBytes, int receivedBytes, long speed) {
        this.IP = IP;
        this.URI = URI;
        this.timeStamp = new Date();
        this.sentBytes = sentBytes;
        this.receivedBytes = receivedBytes;
        this.speed = speed;
    }

    public String getIP() {
        return IP;
    }

    public String getURI() {
        return URI;
    }

    public Date getTimeStamp() {
        return timeStamp;
    }

    public long getSentBytes() {
        return sentBytes;
    }
   
    public long getReceivedBytes() {
        return receivedBytes;
    }

    public long getSpeed() {
        return speed;
    }

    @Override
    public String toString() {
        return "ConnectionLogUnit{" 
                + "IP='" + IP + '\'' + ", URI='" + URI + '\'' +
                ", timeStamp=" + timeStamp + ", sentBytes=" + sentBytes + 
                ", receivedBytes=" + receivedBytes +", speed=" + speed +
                '}';
    }

}
