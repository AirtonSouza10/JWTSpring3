package com.service.desk.enumerator;

import lombok.Getter;

@Getter
public enum MensagemEnum {
	MSGS001("MSGS001"),
	MSGS002("MSGS002"),
	MSGS003("MSGS003"),
	
    MSGE001("MSGE001"),
    MSGE002("MSGE002"),
    MSGE003("MSGE003"),
    MSGE004("MSGE004"),
    MSGE005("MSGE005"),
    MSGE006("MSGE006"),
    MSGE007("MSGE007"),
    MSGE008("MSGE008"),
    MSGE009("MSGE009"),
    MSGE010("MSGE010"),
    MSGE011("MSGE011"),
    MSGE012("MSGE012"),
    MSGE013("MSGE013"),
    MSGE014("MSGE014"),
    MSGE015("MSGE015"),
    MSGE016("MSGE016"),
    MSGE017("MSGE017");

    public String key;

    MensagemEnum(String key) {
        this.key=key;
    }
}
