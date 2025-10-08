package hanniejewelry.vn.product.api;

import hanniejewelry.vn.product.view.CustomCollectionView;

import java.util.List;
import java.util.UUID;


public interface CustomCollectionFacade {
    

    CustomCollectionView getCustomCollectionById(UUID id);
    

    CustomCollectionView getCustomCollectionByHandle(String handle);
    

    List<CustomCollectionView> getAllCustomCollections();
    
    long countCustomCollections();
} 