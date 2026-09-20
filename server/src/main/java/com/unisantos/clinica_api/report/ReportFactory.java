package com.unisantos.clinica_api.report;

import com.unisantos.clinica_api.common.factory.EntityFactory;
import org.springframework.stereotype.Component;

@Component
public class ReportFactory implements EntityFactory<Report, ReportDto> {

  @Override
  public Report create(ReportDto dto) {
    return null;
  }
}
