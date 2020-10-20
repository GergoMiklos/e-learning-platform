import React from 'react';
import {cleanup, render, screen} from '@testing-library/react';
import {MemoryRouter} from 'react-router-dom'
import {formatShortDate, isDateInAWeek} from "../../utils/date-utils";
import GroupListElementCont from "./GroupListElementCont";

jest.mock('../../utils/date-utils');

const mockDateString = '1995-12-17T03:24:00';
const mockGroup = {id: 2, name: 'Group1', news: 'News1', newsChangedDate: new Date(mockDateString)};

describe('GroupListElementContainer', () => {

    afterEach(() => {
        jest.clearAllMocks();
        cleanup();
    });

    test('shows group name and fresh news changed time', async () => {
        isDateInAWeek.mockReturnValue(true);
        formatShortDate.mockReturnValue(mockDateString);

        render(
            <GroupListElementCont group={mockGroup}/>, {wrapper: MemoryRouter}
        );

        await screen.findByRole('link');
        expect(isDateInAWeek).toHaveBeenCalledTimes(1);
        expect(screen.getByText(mockGroup.name)).toBeInTheDocument();
        expect(screen.getByText(mockDateString)).toBeInTheDocument();

    });

    test('does not show not fresh news changed time', async () => {
        isDateInAWeek.mockReturnValue(false);
        formatShortDate.mockReturnValue(mockDateString);

        render(
            <GroupListElementCont group={mockGroup}/>, {wrapper: MemoryRouter}
        );

        await screen.findByRole('link');
        expect(isDateInAWeek).toHaveBeenCalledTimes(1);
        expect(screen.getByText(mockGroup.name)).toBeInTheDocument();
        expect(screen.queryByText(mockDateString)).not.toBeInTheDocument();

    });

});