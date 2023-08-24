package itaipu.gss.sthimporter.dto;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class BDHConfiguracaoSTHDto {
	
	private Integer id;

	private String configString;
	
	private String description;
	
	private Integer maxRemoteId;
	
	private String importerId;
	
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getConfigString() {
		return configString;
	}

	public void setConfigString(String configString) {
		this.configString = configString;
	}
	
	public String getDescription() {
		return description;
	}
	
	public void setDescription(String description) {
		this.description = description;
	}

	public Integer getMaxRemoteId() {
		return maxRemoteId;
	}

	public void setMaxRemoteId(Integer maxRemoteId) {
		this.maxRemoteId = maxRemoteId;
	}

	public String getImportId() {
		return importerId;
	}

	public void setImportId(String importId) {
		this.importerId = importId;
	}
	
	
	
	
}
