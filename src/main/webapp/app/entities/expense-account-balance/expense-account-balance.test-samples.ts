import { IExpenseAccountBalance, NewExpenseAccountBalance } from './expense-account-balance.model';

export const sampleWithRequiredData: IExpenseAccountBalance = {
  id: 33979,
  balance: 78456,
};

export const sampleWithPartialData: IExpenseAccountBalance = {
  id: 45293,
  balance: 77753,
};

export const sampleWithFullData: IExpenseAccountBalance = {
  id: 89495,
  balance: 60438,
};

export const sampleWithNewData: NewExpenseAccountBalance = {
  balance: 36154,
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
