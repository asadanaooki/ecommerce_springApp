package com.example.web.advice;

import org.springframework.context.MessageSource;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;

import com.example.exception.BusinessException;

import lombok.AllArgsConstructor;

/**
 * 例外をハンドラするクラス
 */
@ControllerAdvice
@AllArgsConstructor
public class GlobalExceptionHandler {

    /**
     * メッセージソース
     */
    private MessageSource messageSource;
    
    /**
     * 業務例外をハンドリングする
     * @param e 業務例外
     * @return modelAndView
     */
    @ExceptionHandler(BusinessException.class)
    public ModelAndView handleBusinessException(BusinessException e) {
        String message = messageSource.getMessage(e.getMessageKey(), null, null);
        
        ModelAndView mav = new ModelAndView(e.getViewName());
        mav.addObject("error", message);
        return mav;
    }
    
}
