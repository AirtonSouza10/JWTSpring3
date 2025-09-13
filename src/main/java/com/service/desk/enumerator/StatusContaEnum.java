package com.service.desk.enumerator;

public enum StatusContaEnum {
	
    ABERTA("Aberta"),
    COBRANCA("Pagar"),
    VENCIDA("Vencida"),
    QUITADA("Quitada"),
    NEGOCIADA("Negociada"),
    PARCIAL("Parcialmente Paga"),
    CANCELADA("Cancelada"),
    SUSPENSA("Suspensa");

	private String descricao;
	
	private StatusContaEnum(String descricao) {
		this.descricao = descricao;
	}
	
	public String getDescricao() {
		return descricao;
	}
	
	@Override
	public String toString() {
		return this.getDescricao();
	}
}
