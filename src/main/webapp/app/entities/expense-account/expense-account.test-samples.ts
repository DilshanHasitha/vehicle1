import dayjs from 'dayjs/esm';

import { IExpenseAccount, NewExpenseAccount } from './expense-account.model';

export const sampleWithRequiredData: IExpenseAccount = {
  id: 42597,
  transactionDate: dayjs('2022-11-09'),
  transactionDescription: 'Auto Fantastic transparent',
  transactionAmountDR: 11120,
  transactionAmountCR: 47009,
  transactionBalance: 81311,
};

export const sampleWithPartialData: IExpenseAccount = {
  id: 5332,
  transactionDate: dayjs('2022-11-08'),
  transactionDescription: 'Motorway iterate Tools',
  transactionAmountDR: 89163,
  transactionAmountCR: 26642,
  transactionBalance: 92366,
};

export const sampleWithFullData: IExpenseAccount = {
  id: 31138,
  transactionDate: dayjs('2022-11-09'),
  transactionDescription: 'Associate Fish Card',
  transactionAmountDR: 70132,
  transactionAmountCR: 77471,
  transactionBalance: 56208,
};

export const sampleWithNewData: NewExpenseAccount = {
  transactionDate: dayjs('2022-11-09'),
  transactionDescription: 'HTTP Cotton Multi-lateral',
  transactionAmountDR: 69525,
  transactionAmountCR: 25317,
  transactionBalance: 19088,
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
