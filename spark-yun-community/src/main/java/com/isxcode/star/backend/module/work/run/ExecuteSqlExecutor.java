package com.isxcode.star.backend.module.work.run;

import com.isxcode.star.api.constants.work.WorkLog;
import com.isxcode.star.api.exceptions.WorkRunException;
import com.isxcode.star.backend.module.datasource.DatasourceBizService;
import com.isxcode.star.backend.module.datasource.DatasourceEntity;
import com.isxcode.star.backend.module.datasource.DatasourceRepository;
import com.isxcode.star.backend.module.work.instance.WorkInstanceEntity;
import com.isxcode.star.backend.module.work.instance.WorkInstanceRepository;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.util.Strings;
import org.springframework.stereotype.Service;

import java.sql.Connection;
import java.sql.Statement;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Slf4j
public class ExecuteSqlExecutor extends WorkExecutor {

  private final DatasourceRepository datasourceRepository;

  private final DatasourceBizService datasourceBizService;

  public ExecuteSqlExecutor(WorkInstanceRepository workInstanceRepository, DatasourceRepository datasourceRepository, DatasourceBizService datasourceBizService) {

    super(workInstanceRepository);
    this.datasourceRepository = datasourceRepository;
    this.datasourceBizService = datasourceBizService;
  }

  @Override
  public void execute(WorkRunContext workRunContext, WorkInstanceEntity workInstance) {

    // 获取日志构造器
    StringBuilder logBuilder = workRunContext.getLogBuilder();

    // 检测数据源是否配置
    logBuilder.append(LocalDateTime.now()).append(WorkLog.SUCCESS_INFO).append("开始检测运行环境 \n");
    if (Strings.isEmpty(workRunContext.getDatasourceId())) {
      throw new WorkRunException(LocalDateTime.now() + WorkLog.ERROR_INFO + "检测运行环境失败: 未配置有效数据源  \n");
    }

    // 检查数据源是否存在
    Optional<DatasourceEntity> datasourceEntityOptional = datasourceRepository.findById(workRunContext.getDatasourceId());
    if (!datasourceEntityOptional.isPresent()) {
      throw new WorkRunException(LocalDateTime.now() + WorkLog.ERROR_INFO + "检测运行环境失败: 未配置有效数据源  \n");
    }

    // 数据源检查通过
    logBuilder.append(LocalDateTime.now()).append(WorkLog.SUCCESS_INFO).append("检测运行环境完成  \n");
    workInstance = updateInstance(workInstance, logBuilder);

    // 检查脚本是否为空
    if (Strings.isEmpty(workRunContext.getSqlScript())) {
      throw new WorkRunException(LocalDateTime.now() + WorkLog.ERROR_INFO + "Sql内容为空 \n");
    }

    // 脚本检查通过
    logBuilder.append(LocalDateTime.now()).append(WorkLog.SUCCESS_INFO).append("开始执行作业 \n");
    workInstance = updateInstance(workInstance, logBuilder);

    // 开始执行作业
    try (Connection connection = datasourceBizService.getDbConnection(datasourceEntityOptional.get());
         Statement statement = connection.createStatement()) {

      // 清除注释
      String noCommentSql = workRunContext.getSqlScript().replaceAll("/\\*(?:.|[\\n\\r])*?\\*/|--.*", "");

      // 清除脚本中的脏数据
      List<String> sqls = Arrays.stream(noCommentSql.split(";")).filter(e -> !Strings.isEmpty(e)).collect(Collectors.toList());

      // 逐条执行sql
      for (String sql : sqls) {

        // 记录开始执行时间
        logBuilder.append(LocalDateTime.now()).append(WorkLog.SUCCESS_INFO).append("开始执行SQL: ").append(sql).append(" \n");
        workInstance = updateInstance(workInstance, logBuilder);

        // 执行sql
        statement.execute(sql);

        // 记录结束执行时间
        logBuilder.append(LocalDateTime.now()).append(WorkLog.SUCCESS_INFO).append("SQL执行成功  \n");
        workInstance = updateInstance(workInstance, logBuilder);
      }
    } catch (Exception e) {

      log.error(e.getMessage());
      throw new WorkRunException(LocalDateTime.now() + WorkLog.ERROR_INFO + e.getMessage() + "\n");
    }
  }
}