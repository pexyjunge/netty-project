package com.yjz.time.pojo;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.Date;

@Data
@AllArgsConstructor
public class UnixTime {


    private long value;

    public UnixTime() {
        this(System.currentTimeMillis() / 1000L + 2208988800L);
    }

    public String toString() {
        return new Date((getValue() - 2208988800L) * 1000L).toString();
    }
}
