import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { ICashBookBalance } from '../cash-book-balance.model';
import { sampleWithRequiredData, sampleWithNewData, sampleWithPartialData, sampleWithFullData } from '../cash-book-balance.test-samples';

import { CashBookBalanceService } from './cash-book-balance.service';

const requireRestSample: ICashBookBalance = {
  ...sampleWithRequiredData,
};

describe('CashBookBalance Service', () => {
  let service: CashBookBalanceService;
  let httpMock: HttpTestingController;
  let expectedResult: ICashBookBalance | ICashBookBalance[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(CashBookBalanceService);
    httpMock = TestBed.inject(HttpTestingController);
  });

  describe('Service methods', () => {
    it('should find an element', () => {
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.find(123).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should create a CashBookBalance', () => {
      // eslint-disable-next-line @typescript-eslint/no-unused-vars
      const cashBookBalance = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(cashBookBalance).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a CashBookBalance', () => {
      const cashBookBalance = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(cashBookBalance).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a CashBookBalance', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of CashBookBalance', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a CashBookBalance', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addCashBookBalanceToCollectionIfMissing', () => {
      it('should add a CashBookBalance to an empty array', () => {
        const cashBookBalance: ICashBookBalance = sampleWithRequiredData;
        expectedResult = service.addCashBookBalanceToCollectionIfMissing([], cashBookBalance);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(cashBookBalance);
      });

      it('should not add a CashBookBalance to an array that contains it', () => {
        const cashBookBalance: ICashBookBalance = sampleWithRequiredData;
        const cashBookBalanceCollection: ICashBookBalance[] = [
          {
            ...cashBookBalance,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addCashBookBalanceToCollectionIfMissing(cashBookBalanceCollection, cashBookBalance);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a CashBookBalance to an array that doesn't contain it", () => {
        const cashBookBalance: ICashBookBalance = sampleWithRequiredData;
        const cashBookBalanceCollection: ICashBookBalance[] = [sampleWithPartialData];
        expectedResult = service.addCashBookBalanceToCollectionIfMissing(cashBookBalanceCollection, cashBookBalance);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(cashBookBalance);
      });

      it('should add only unique CashBookBalance to an array', () => {
        const cashBookBalanceArray: ICashBookBalance[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const cashBookBalanceCollection: ICashBookBalance[] = [sampleWithRequiredData];
        expectedResult = service.addCashBookBalanceToCollectionIfMissing(cashBookBalanceCollection, ...cashBookBalanceArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const cashBookBalance: ICashBookBalance = sampleWithRequiredData;
        const cashBookBalance2: ICashBookBalance = sampleWithPartialData;
        expectedResult = service.addCashBookBalanceToCollectionIfMissing([], cashBookBalance, cashBookBalance2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(cashBookBalance);
        expect(expectedResult).toContain(cashBookBalance2);
      });

      it('should accept null and undefined values', () => {
        const cashBookBalance: ICashBookBalance = sampleWithRequiredData;
        expectedResult = service.addCashBookBalanceToCollectionIfMissing([], null, cashBookBalance, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(cashBookBalance);
      });

      it('should return initial array if no CashBookBalance is added', () => {
        const cashBookBalanceCollection: ICashBookBalance[] = [sampleWithRequiredData];
        expectedResult = service.addCashBookBalanceToCollectionIfMissing(cashBookBalanceCollection, undefined, null);
        expect(expectedResult).toEqual(cashBookBalanceCollection);
      });
    });

    describe('compareCashBookBalance', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareCashBookBalance(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 123 };
        const entity2 = null;

        const compareResult1 = service.compareCashBookBalance(entity1, entity2);
        const compareResult2 = service.compareCashBookBalance(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 456 };

        const compareResult1 = service.compareCashBookBalance(entity1, entity2);
        const compareResult2 = service.compareCashBookBalance(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 123 };

        const compareResult1 = service.compareCashBookBalance(entity1, entity2);
        const compareResult2 = service.compareCashBookBalance(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
