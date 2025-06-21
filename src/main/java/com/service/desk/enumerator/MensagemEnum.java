package com.service.desk.enumerator;

import lombok.Getter;

@Getter
public enum MensagemEnum {
	MSGS001("MSGS001"),
	MSGS002("MSGS002"),
	
    MSGE001("MSGE001"),
    MSGE002("MSGE002"),
    MSGE003("MSGE003"),
    MSGE004("MSGE004"),
    MSGE005("MSGE005"),
    MSGE006("MSGE006"),
    MSGE007("MSGE007"),
    MSGE008("MSGE008"),
    MSGE009("MSGE009"),
    MSGE010("MSGE010");

    public String key;

    MensagemEnum(String key) {
        this.key=key;
    }
}
