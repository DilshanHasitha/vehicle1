import { IVehicle, NewVehicle } from './vehicle.model';

export const sampleWithRequiredData: IVehicle = {
  id: 47420,
  code: 'invoice Sausages tan',
  name: 'Jewelery',
};

export const sampleWithPartialData: IVehicle = {
  id: 2304,
  code: 'Health Canada invoice',
  name: 'Delaware archive',
  relatedUserLogin: '5th Legacy',
  isActive: false,
};

export const sampleWithFullData: IVehicle = {
  id: 87707,
  code: 'support',
  name: 'mission-critical black HDD',
  relatedUserLogin: 'Fish',
  expenceCode: 'Kansas',
  isActive: false,
};

export const sampleWithNewData: NewVehicle = {
  code: 'monitor',
  name: 'Drive',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
