package itaipu.gss.sthimporter.model;

public class ConfiguracaoSTH {

	private Integer id;

	private String configString;
	
	private String description;
	
	private Integer maxRemoteId;
	
	private String importerId;

	public static ConfiguracaoSTH build(Integer id, String configString, String description, Integer maxRemoteId, String importId) {
		ConfiguracaoSTH configuracaoSTH = new ConfiguracaoSTH();
		
		configuracaoSTH.setId(id);
		configuracaoSTH.setImporterId(importId);
		configuracaoSTH.setConfigString(configString);
		configuracaoSTH.setDescription(description);
		configuracaoSTH.setMaxRemoteId(maxRemoteId);
		
		return configuracaoSTH;
	}

	public Integer getId() {
		return id;
	}

	public String getConfigString() {
		return configString;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public void setConfigString(String filename) {
		this.configString = filename;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ConfiguracaoSTH other = (ConfiguracaoSTH) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}
	
	@Override
	public String toString() {
		return "STH [filename=" + configString + "]";
	}

	public Integer getMaxRemoteId() {
		return maxRemoteId;
	}

	public void setMaxRemoteId(Integer maxRemoteId) {
		this.maxRemoteId = maxRemoteId;
	}

	public String getImporterId() {
		return importerId;
	}

	public void setImporterId(String importerId) {
		this.importerId = importerId;
	}
}
