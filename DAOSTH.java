package itaipu.gss.sthimporter.dao;

import java.util.List;

import itaipu.gss.framework.exception.AppException;
import itaipu.gss.sthimporter.dto.BDHMeasureValueDto;

public interface DAOSTH {
	
	public List<BDHMeasureValueDto> getMeasures() throws AppException;
    
	public Integer getMaxtRemoteId();

}
