package itaipu.gss.sthimporter.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import itaipu.gss.framework.exception.AppException;
import itaipu.gss.framework.log.AppLogger;
import itaipu.gss.sthimporter.dao.DAOBDHWebService;
import itaipu.gss.sthimporter.dao.DAOSTH;
import itaipu.gss.sthimporter.dao.DAOSTHFactory;
import itaipu.gss.sthimporter.dto.BDHConfiguracaoSTHDto;
import itaipu.gss.sthimporter.dto.BDHMeasureDto;
import itaipu.gss.sthimporter.dto.BDHMeasureListDto;
import itaipu.gss.sthimporter.dto.BDHMeasureValueDto;
import itaipu.gss.sthimporter.model.ConfiguracaoSTH;

public class STHController {

	public static final String APP_NAME = "STHImporter";
	
	public void importMeasures(String importerId, Date importTimestamp) throws Exception {		
		long startExecutionTime = System.currentTimeMillis();
		AppLogger.logInfoMessage("Starting import procedure...");
		
		DAOBDHWebService daoBDH = new DAOBDHWebService();

		List<BDHConfiguracaoSTHDto> bdhConfiguracaoSTHDto = daoBDH.getListConfiguracaoSTH(importerId, importTimestamp);
		AppLogger.logInfoMessage("Getting configuration STH list from BDH. Importer ID: "+ importerId);
		List<ConfiguracaoSTH> configuracaoSTHList = montaListaConfiguracaoSTH(bdhConfiguracaoSTHDto);
		
		AppLogger.logInfoMessage("Getting measures from " + configuracaoSTHList.size() + " stations.");
		List<BDHMeasureDto> bdhMeasureDtoList = getBDHMeasureDtoList(importTimestamp, configuracaoSTHList);
		
		int changesCount = 0;
		if (!bdhMeasureDtoList.isEmpty()) {
			AppLogger.logInfoMessage("Sending measures to BDH. List size: " + bdhMeasureDtoList.size());
			changesCount = save(importTimestamp, daoBDH, bdhMeasureDtoList);
		} else {
			AppLogger.logInfoMessage("There is no measure to send to BDH. bdhMeasureDtoList is empty.");
		}
		
		daoBDH.logout();		

		long executionTime = (System.currentTimeMillis() - startExecutionTime) / 1000;
		AppLogger.logInfoMessage(
				"Execução finalizada em " + executionTime + "s." + " Medidas novas ou alteradas: " + changesCount);		
	}

	private List<BDHMeasureDto> getBDHMeasureDtoList(Date importTimestamp, List<ConfiguracaoSTH> configuracaoSTHList) {
		List<BDHMeasureDto> bdhMeasureDtoList = new ArrayList<BDHMeasureDto>();
		for (ConfiguracaoSTH configuracaoSTH : configuracaoSTHList) {
			try {
				DAOSTH daoSTH = DAOSTHFactory.buildDAOSTH(configuracaoSTH);
				List<BDHMeasureValueDto> measureList = daoSTH.getMeasures();
				// Envia as medidas da estação se a lista não estiver vazia ou se tiver alguma atualização no maxRemoteId.
				if (!measureList.isEmpty() || daoSTH.getMaxtRemoteId() > configuracaoSTH.getMaxRemoteId()) {
					BDHMeasureDto bdhMeasureDto = new BDHMeasureDto(configuracaoSTH.getId(),
                                                configuracaoSTH.getDescription(),
                                                configuracaoSTH.getMaxRemoteId(),
                                                daoSTH.getMaxtRemoteId(),
                                                importTimestamp,
                                                measureList);
					bdhMeasureDtoList.add(bdhMeasureDto);					
				} else {
					AppLogger.logWarningMessage("Lista de Medidas vazia descartada para a configuração: " + configuracaoSTH.getDescription());
				}
			} catch (Exception e) {
				e.printStackTrace();
				AppLogger.logErrorMessage("Fail to process station configuration: " + configuracaoSTH.getDescription() 
					+ " Error message: " + e.getMessage(), e);
			}
		}
		return bdhMeasureDtoList;
	}

	private int save(Date importTimestamp, DAOBDHWebService daoBDH, List<BDHMeasureDto> bdhMeasureDtoList) throws AppException {
		BDHMeasureListDto bdhMeasureListDto = new BDHMeasureListDto();
		bdhMeasureListDto.setMeasureDtoList(bdhMeasureDtoList);
		bdhMeasureListDto.setImportTimestamp(importTimestamp);
		bdhMeasureListDto.setMeasureListSize(bdhMeasureDtoList.size());
		
		return daoBDH.save(bdhMeasureListDto);
	}

	private List<ConfiguracaoSTH> montaListaConfiguracaoSTH(List<BDHConfiguracaoSTHDto> sthConfigDtoList) {
		List<ConfiguracaoSTH> sthConfigList = new ArrayList<ConfiguracaoSTH>();
		for (BDHConfiguracaoSTHDto sthConfig : sthConfigDtoList) {
			sthConfigList.add(ConfiguracaoSTH.build(
					sthConfig.getId(), 
					sthConfig.getConfigString(), 
					sthConfig.getDescription(),
					sthConfig.getMaxRemoteId(),
					sthConfig.getImportId()));
		}
		return sthConfigList;

	}
	

}
