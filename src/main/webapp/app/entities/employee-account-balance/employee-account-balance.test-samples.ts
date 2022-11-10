import { IEmployeeAccountBalance, NewEmployeeAccountBalance } from './employee-account-balance.model';

export const sampleWithRequiredData: IEmployeeAccountBalance = {
  id: 50455,
  balance: 7916,
};

export const sampleWithPartialData: IEmployeeAccountBalance = {
  id: 47035,
  balance: 57857,
};

export const sampleWithFullData: IEmployeeAccountBalance = {
  id: 25066,
  balance: 86389,
};

export const sampleWithNewData: NewEmployeeAccountBalance = {
  balance: 92598,
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
