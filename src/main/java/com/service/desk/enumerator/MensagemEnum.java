package com.service.desk.enumerator;

import lombok.Getter;

@Getter
public enum MensagemEnum {
	MSGS001("MSGS001"),
	
    MSGE001("MSGE001"),
    MSGE002("MSGE002"),
    MSGE003("MSGE003"),
    MSGE004("MSGE004"),
    MSGE005("MSGE005");

    public String key;

    MensagemEnum(String key) {
        this.key=key;
    }
}
