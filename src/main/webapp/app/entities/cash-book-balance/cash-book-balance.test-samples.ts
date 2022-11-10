import { ICashBookBalance, NewCashBookBalance } from './cash-book-balance.model';

export const sampleWithRequiredData: ICashBookBalance = {
  id: 65648,
  balance: 74876,
};

export const sampleWithPartialData: ICashBookBalance = {
  id: 89536,
  balance: 79271,
};

export const sampleWithFullData: ICashBookBalance = {
  id: 8889,
  balance: 17950,
};

export const sampleWithNewData: NewCashBookBalance = {
  balance: 72914,
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
