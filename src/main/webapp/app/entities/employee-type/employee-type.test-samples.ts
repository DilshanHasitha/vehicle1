import { IEmployeeType, NewEmployeeType } from './employee-type.model';

export const sampleWithRequiredData: IEmployeeType = {
  id: 98974,
  code: 'Plastic neural',
  name: 'stable',
};

export const sampleWithPartialData: IEmployeeType = {
  id: 72545,
  code: 'bypass communities',
  name: 'Representative web index',
  isActive: true,
};

export const sampleWithFullData: IEmployeeType = {
  id: 16575,
  code: 'extranet markets digital',
  name: 'Unbranded Cotton',
  isActive: false,
};

export const sampleWithNewData: NewEmployeeType = {
  code: 'connect Functionality',
  name: 'Product SMS Chicken',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
