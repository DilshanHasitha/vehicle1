import { IExpenseType, NewExpenseType } from './expense-type.model';

export const sampleWithRequiredData: IExpenseType = {
  id: 54415,
  code: 'Engineer',
  name: 'facilitate JSON Paradigm',
};

export const sampleWithPartialData: IExpenseType = {
  id: 1842,
  code: 'Pants Meadow Fresh',
  name: 'Loan website Costa',
};

export const sampleWithFullData: IExpenseType = {
  id: 18693,
  code: 'Avon product',
  name: 'Salad Representative Nicaragua',
  isActive: false,
};

export const sampleWithNewData: NewExpenseType = {
  code: 'Human Views RSS',
  name: 'Virginia',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
