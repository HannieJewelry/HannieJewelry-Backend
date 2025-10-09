package hanniejewelry.vn.shared.utils;


import com.blazebit.persistence.PagedList;
import hanniejewelry.vn.shared.dto.GenericFilterRequest;
import hanniejewelry.vn.shared.dto.PagedResponse;

public final class ResponseUtils {
  private ResponseUtils() {}

  public static <T> PagedResponse<T> pagedSuccess(
          final PagedList<T> result, final GenericFilterRequest filter
  ) {
    return new PagedResponse<>(
            result,
            filter.getPage(),
            filter.getSize(),
            result.getTotalSize(),
            filter.getSortProperty(),
            filter.getDirection()
    );
  }

}
