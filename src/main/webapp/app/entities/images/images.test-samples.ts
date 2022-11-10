import { IImages, NewImages } from './images.model';

export const sampleWithRequiredData: IImages = {
  id: 22223,
};

export const sampleWithPartialData: IImages = {
  id: 66466,
  imglobContentType: 'Montana Buckinghamshire',
  originalURL: 'index Trail dynamic',
};

export const sampleWithFullData: IImages = {
  id: 6894,
  imglobContentType: 'Mission IB',
  imageURL: 'Pizza',
  imageName: 'payment of Executive',
  lowResURL: 'Arizona Tasty Pizza',
  originalURL: 'encompassing',
  imageBlob: '../fake-data/blob/hipster.png',
  imageBlobContentType: 'unknown',
};

export const sampleWithNewData: NewImages = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
