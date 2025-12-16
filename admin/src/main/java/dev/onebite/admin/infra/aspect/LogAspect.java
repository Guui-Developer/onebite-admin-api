package dev.onebite.admin.infra.aspect;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.annotation.Aspect;

@Slf4j
@Aspect
public class LogAspect {

    private static final String POINTCUT = "execution(* dev.onebite.admin.application.service..*(..))";


}
