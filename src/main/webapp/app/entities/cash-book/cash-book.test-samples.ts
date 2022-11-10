import dayjs from 'dayjs/esm';

import { ICashBook, NewCashBook } from './cash-book.model';

export const sampleWithRequiredData: ICashBook = {
  id: 3643,
  transactionDate: dayjs('2022-11-08'),
  transactionDescription: 'Consultant',
  transactionAmountDR: 90282,
  transactionAmountCR: 64258,
  transactionBalance: 60410,
};

export const sampleWithPartialData: ICashBook = {
  id: 29270,
  transactionDate: dayjs('2022-11-09'),
  transactionDescription: 'Consultant protocol schemas',
  transactionAmountDR: 47894,
  transactionAmountCR: 18087,
  transactionBalance: 87206,
};

export const sampleWithFullData: ICashBook = {
  id: 52739,
  transactionDate: dayjs('2022-11-09'),
  transactionDescription: 'partnerships',
  transactionAmountDR: 92185,
  transactionAmountCR: 5216,
  transactionBalance: 31490,
};

export const sampleWithNewData: NewCashBook = {
  transactionDate: dayjs('2022-11-09'),
  transactionDescription: 'override Tools',
  transactionAmountDR: 44622,
  transactionAmountCR: 78998,
  transactionBalance: 55565,
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
