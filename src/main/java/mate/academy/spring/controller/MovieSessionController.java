package mate.academy.spring.controller;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;
import mate.academy.spring.dto.MovieSessionRequestDto;
import mate.academy.spring.dto.MovieSessionResponseDto;
import mate.academy.spring.mapper.MovieSessionDtoMapper;
import mate.academy.spring.model.MovieSession;
import mate.academy.spring.service.MovieSessionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/movie-sessions")
public class MovieSessionController {
    private final MovieSessionService movieSessionService;
    private final MovieSessionDtoMapper movieSessionDtoMapper;

    @Autowired
    public MovieSessionController(
            MovieSessionService movieSessionService, MovieSessionDtoMapper movieSessionDtoMapper
    ) {
        this.movieSessionService = movieSessionService;
        this.movieSessionDtoMapper = movieSessionDtoMapper;
    }

    @PostMapping
    public MovieSessionResponseDto create(
            @RequestBody MovieSessionRequestDto movieSessionRequestDto
    ) {
        MovieSession movieSession =
                movieSessionService.add(movieSessionDtoMapper.toModel(movieSessionRequestDto));
        return movieSessionDtoMapper.toDto(movieSession);
    }

    @GetMapping("/available")
    public List<MovieSessionResponseDto> getAllAvailableAtDateByMovieId(
            @RequestParam Long movieId,
            @RequestParam @DateTimeFormat(pattern = "dd.MM.yyyy") LocalDate date
    ) {
        return movieSessionService
                .findAvailableSessions(movieId, date)
                .stream()
                .map(movieSessionDtoMapper::toDto)
                .collect(Collectors.toList());
    }

    @PutMapping("/{id}")
    public MovieSessionResponseDto update(
            @PathVariable Long movieId,
            @RequestBody MovieSessionRequestDto movieSessionRequestDto
    ) {
        MovieSession movieSession = movieSessionDtoMapper.toModel(movieSessionRequestDto);
        movieSession.setId(movieId);
        return movieSessionDtoMapper.toDto(movieSessionService.update(movieSession));
    }

    @DeleteMapping("/{id}")
    public String delete(@PathVariable Long movieId) {
        movieSessionService.delete(movieId);
        return "Done!";
    }
}