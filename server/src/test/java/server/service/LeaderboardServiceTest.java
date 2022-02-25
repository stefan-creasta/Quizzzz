package server.service;

import commons.LeaderboardEntry;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.Pageable;
import server.database.LeaderboardRepository;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class LeaderboardServiceTest {
    LeaderboardRepository repo = mock(LeaderboardRepository.class);
    LeaderboardService service = new LeaderboardService(repo);

    Date d = new Date();
    List<LeaderboardEntry> leaderboardEntries = List.of(
            new LeaderboardEntry("Tina", 5000, d),
            new LeaderboardEntry("Tony", 2500, d),
            new LeaderboardEntry("Sally", 3000, d)
    );

    @Test
    public void addEntryTest() {
        when(repo.save(null)).thenThrow(IllegalArgumentException.class);

        service.addEntry(leaderboardEntries.get(0));

        assertThrows(IllegalArgumentException.class, () -> {
            service.addEntry(null);
        });
    }

    @Test
    public void getLeaderboardTest() {
        when(repo.findAllSorted()).thenReturn(leaderboardEntries.stream().sorted().collect(Collectors.toList()));
        when(repo.findTopN(any(Pageable.class))).thenAnswer(
                I -> leaderboardEntries.stream().sorted().limit(
                        ((Pageable) I.getArgument(0)).getPageSize()
                ).collect(Collectors.toList())
        );

        assertEquals(List.of(leaderboardEntries.get(0), leaderboardEntries.get(2)), service.getLeaderboard(2));
        assertEquals(leaderboardEntries.stream().sorted().collect(Collectors.toList()), service.getLeaderboard(-1));
    }

    @Test
    public void orderAndCutLeaderboardTest() {
        when(repo.findTopN(any(Pageable.class))).thenAnswer(
                I -> leaderboardEntries.stream().sorted().limit(
                        ((Pageable) I.getArgument(0)).getPageSize()
                ).collect(Collectors.toList())
        );

        service.orderAndCutLeaderboard(10);
    }
}
