import { IBanners, NewBanners } from './banners.model';

export const sampleWithRequiredData: IBanners = {
  id: 13933,
};

export const sampleWithPartialData: IBanners = {
  id: 85331,
  code: 'synthesizing Frozen',
};

export const sampleWithFullData: IBanners = {
  id: 58382,
  code: 'Loan Outdoors Avon',
  heading: 'holistic white Pants',
  description: 'Architect',
  link: 'transmit programming',
};

export const sampleWithNewData: NewBanners = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
