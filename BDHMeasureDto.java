package itaipu.gss.sthimporter.dto;

import java.util.Date;
import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;

import com.fasterxml.jackson.annotation.JsonFormat;

@XmlRootElement(name="measureDto")
public class BDHMeasureDto {
	
	private Integer configuracaoSTHId;
	
	@JsonFormat(shape=JsonFormat.Shape.STRING, pattern="yyyy-MM-ddTHH:mm:ss")
	private Date importTimestamp;
	
	private List<BDHMeasureValueDto> measureValueDtoList;

	private Integer initRemoteId;
	
	private Integer maxRemoteId;
	
	private String configSTHDesc;
	
	public BDHMeasureDto() {}

	public BDHMeasureDto(Integer configuracaoSTHId, String configSTHDesc, Integer initRemoteId, 
			Integer maxRemoteId, Date importTimestamp, List<BDHMeasureValueDto> measureValueDtoList) {
		
		this.configuracaoSTHId = configuracaoSTHId;
		this.configSTHDesc = configSTHDesc;
		this.initRemoteId = initRemoteId;
		this.maxRemoteId = maxRemoteId;
		this.importTimestamp = importTimestamp;
		this.measureValueDtoList = measureValueDtoList;
	}

	public Integer getConfiguracaoSTHId() {
		return configuracaoSTHId;
	}

	public void setConfiguracaoSTHId(Integer configuracaoSTHId) {
		this.configuracaoSTHId = configuracaoSTHId;
	}

	public List<BDHMeasureValueDto> getMeasureValueDtoList() {
		return measureValueDtoList;
	}

	public void setMeasureValueDtoList(List<BDHMeasureValueDto> measureValueDtoList) {
		this.measureValueDtoList = measureValueDtoList;
	}

	@Override
	public String toString() {
		return "MeasureDto [configuracaoSTHId=" + configuracaoSTHId + ", measureValueDtoList=" + measureValueDtoList
				+ "]";
	}

	public Date getImportTimestamp() {
		return importTimestamp;
	}

	public void setImportTimestamp(Date importTimestamp) {
		this.importTimestamp = importTimestamp;
	}

	public Integer getMaxRemoteId() {
		return maxRemoteId;
	}

	public void setMaxRemoteId(Integer maxRemoteId) {
		this.maxRemoteId = maxRemoteId;
	}

	public String getConfigSTHDesc() {
		return configSTHDesc;
	}

	public void setConfigSTHDesc(String configSTHDesc) {
		this.configSTHDesc = configSTHDesc;
	}

	public Integer getInitRemoteId() {
		return initRemoteId;
	}

	public void setInitRemoteId(Integer initRemoteId) {
		this.initRemoteId = initRemoteId;
	}

}