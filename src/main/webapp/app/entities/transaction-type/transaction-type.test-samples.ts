import { ITransactionType, NewTransactionType } from './transaction-type.model';

export const sampleWithRequiredData: ITransactionType = {
  id: 73897,
  code: 'Awesome Handcrafted Flats',
  description: 'zero Nevada grey',
};

export const sampleWithPartialData: ITransactionType = {
  id: 28231,
  code: 'View',
  description: 'pink compressing transmitter',
  isActive: false,
};

export const sampleWithFullData: ITransactionType = {
  id: 55575,
  code: 'synergistic Incredible Prairie',
  description: 'Vatu',
  isActive: true,
};

export const sampleWithNewData: NewTransactionType = {
  code: 'Frozen',
  description: 'Specialist Faroe digital',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
