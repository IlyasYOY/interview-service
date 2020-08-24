package com.github.interview.interviewservice.converter;

import com.github.interview.interviewservice.domain.value.Position;
import com.github.interview.interviewservice.model.PositionModel;
import lombok.RequiredArgsConstructor;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PositionToPositionModelConverter implements Converter<Position, PositionModel> {
    @Override
    public PositionModel convert(Position source) {
        return PositionModel.builder()
                .column(source.getColumn())
                .line(source.getLine())
                .build();
    }
}
