package com.danieljoanol.forms.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;

import org.jeasy.random.EasyRandom;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.danieljoanol.forms.entity.TokenBlacklist;
import com.danieljoanol.forms.repository.TokenBlacklistRepository;

@ExtendWith(MockitoExtension.class)
public class TokenBlacklistServiceImplTest {
  
  @Mock
  private TokenBlacklistRepository blacklistRepository;

  @InjectMocks
  private TokenBlacklistServiceImpl service;

  private static final EasyRandom generator = new EasyRandom();
  private static final String TOKEN = "g877sa987d9";
  private static final Boolean BOOL = true;

  private TokenBlacklist blacklisted;

  @BeforeEach
  void setUp() {
    blacklisted = generator.nextObject(TokenBlacklist.class);
  }

  @Test
  void whenSave_thenReturnTokeBlackList() {

    when(blacklistRepository.save(any(TokenBlacklist.class))).thenReturn(blacklisted);

    final TokenBlacklist response = service.save(TOKEN);
    assertEquals(blacklisted.getToken(), response.getToken());
  }

  @Test
  void whenDeleteByDate_thenVerify() {

    service.deleteByDate(LocalDateTime.now());
    verify(blacklistRepository, times(1)).deleteByDateBefore(any(LocalDateTime.class));
  }

  @Test
  void whenExistsByToken_thenReturnBoolean() {

    when(blacklistRepository.existsByToken(anyString())).thenReturn(BOOL);

    final Boolean response = service.existsByToken(TOKEN);
    assertEquals(BOOL, response);
  }

}
