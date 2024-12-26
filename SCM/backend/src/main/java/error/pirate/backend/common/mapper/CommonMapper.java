package error.pirate.backend.common.mapper;

import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface CommonMapper {
    String nameGenerator(String domainName);

    List<Integer> remainingQuantity(String selectDomainName, String joinDomainName, long seq);
}
