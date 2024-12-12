package error.pirate.backend.common.mapper;

import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface CommonMapper {
    String nameGenerator(String domainName);
}
